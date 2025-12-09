package com.dormclean.dorm_cleaning_management.service;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.dormclean.dorm_cleaning_management.entity.Dorm;
import com.dormclean.dorm_cleaning_management.entity.Room;
import com.dormclean.dorm_cleaning_management.entity.enums.RoomStatus;
import com.dormclean.dorm_cleaning_management.repository.DormRepository;
import com.dormclean.dorm_cleaning_management.repository.RoomRepository;

@Service
@RequiredArgsConstructor
public class ExcelServiceImpl implements ExcelService {
    private final DormRepository dormRepository;
    private final RoomRepository roomRepository;

    @Override
    public void downloadExcel(HttpServletResponse res) throws IOException {

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("RoomInfo");
        sheet.setDefaultColumnWidth(28);

        // Header Font Style
        XSSFFont headerFont = (XSSFFont) workbook.createFont();
        headerFont.setColor(new XSSFColor(new byte[] { (byte) 255, (byte) 255, (byte) 255 }));

        // Header Cell Style
        XSSFCellStyle headerStyle = (XSSFCellStyle) workbook.createCellStyle();
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setFillForegroundColor(new XSSFColor(new byte[] { 34, 37, 41 }));
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setFont(headerFont);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        // Body Cell Style
        XSSFCellStyle bodyStyle = (XSSFCellStyle) workbook.createCellStyle();
        bodyStyle.setBorderLeft(BorderStyle.THIN);
        bodyStyle.setBorderRight(BorderStyle.THIN);
        bodyStyle.setBorderTop(BorderStyle.THIN);
        bodyStyle.setBorderBottom(BorderStyle.THIN);
        bodyStyle.setAlignment(HorizontalAlignment.CENTER);
        bodyStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        XSSFCellStyle alignStyle = (XSSFCellStyle) workbook.createCellStyle();
        alignStyle.setAlignment(HorizontalAlignment.CENTER);
        alignStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        // Header 생성
        int rowCount = 0;
        String[] headerNames = { "건물 명", "호실 번호", "**반드시 텍스트로 입력**" };
        Row headerRow = sheet.createRow(rowCount++);

        for (int i = 0; i < headerNames.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headerNames[i]);
            if (i < headerNames.length - 1) {
                cell.setCellStyle(headerStyle);
            }
        }

        // Body 데이터 예시
        String[][] bodyData = {
                { "101", "201" },
        };

        for (String[] rowArr : bodyData) {
            Row row = sheet.createRow(rowCount++);
            for (int i = 0; i < rowArr.length; i++) {
                Cell cell = row.createCell(i);
                cell.setCellValue(rowArr[i]);
                cell.setCellStyle(bodyStyle);
            }
        }

        // 조건부 서식
        SheetConditionalFormatting sheetCF = sheet.getSheetConditionalFormatting();
        ConditionalFormattingRule rule = sheetCF.createConditionalFormattingRule("OR(LEN($A2)>0, LEN($B2)>0)");

        BorderFormatting border = rule.createBorderFormatting();
        border.setBorderLeft(BorderStyle.THIN);
        border.setBorderRight(BorderStyle.THIN);
        border.setBorderTop(BorderStyle.THIN);
        border.setBorderBottom(BorderStyle.THIN);

        CellRangeAddress[] regions = {
                CellRangeAddress.valueOf("A2:B500")
        };
        sheetCF.addConditionalFormatting(regions, rule);

        // Excel 출력
        ServletOutputStream out = res.getOutputStream();
        workbook.write(out);
        workbook.close();
        out.flush();
        out.close();
    }

    @Override
    @Transactional
    public void registerByExcel(MultipartFile file) throws Exception {
        try (InputStream inputStream = file.getInputStream()) {
            try (Workbook workbook = new XSSFWorkbook(inputStream)) {
                Sheet sheet = workbook.getSheetAt(0);

                for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                    Row row = sheet.getRow(i);
                    if (row == null)
                        continue;

                    DataFormatter formatter = new DataFormatter();

                    String dormCode = formatter.formatCellValue(row.getCell(0));
                    String roomNumber = formatter.formatCellValue(row.getCell(1));

                    System.out.println(dormCode + ", " + roomNumber);

                    if (dormCode.isEmpty() || roomNumber.isEmpty())
                        continue;

                    // 1. 기숙사 존재 확인
                    Dorm dorm = dormRepository.findByDormCode(dormCode)
                            .orElseGet(() -> Dorm.builder()
                                    .dormCode(dormCode)
                                    .dormName(dormCode)
                                    .build());
                    if (dorm.getId() == null) {
                        dorm = dormRepository.save(dorm);
                    }

                    // 2. 호실 존재 체크
                    boolean exists = roomRepository.existsByDormAndRoomNumber(dorm, roomNumber);

                    if (exists) {
                        continue; // 이미 존재 → PASS
                    }

                    Integer floor = extractFloor(roomNumber);

                    // 3. 신규 호실 저장
                    Room room = Room.builder()
                            .dorm(dorm)
                            .floor(floor)
                            .roomNumber(roomNumber)
                            .status(RoomStatus.READY)
                            .build();

                    roomRepository.save(room);
                }
            }
        }
    }

    private Integer extractFloor(String roomNumber) {
        int cnt = 0, idx = roomNumber.length() - 1;
        while (idx >= 0) {
            char c = roomNumber.charAt(idx);
            if (c >= '0' && c <= '9')
                break;
            cnt++;
            idx--;
        }
        int floor = Integer.parseInt(roomNumber.substring(0, roomNumber.length() - (cnt + 2)));

        return floor;
    }
}
