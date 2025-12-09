package com.dormclean.dorm_cleaning_management.service;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.io.InputStream;

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

        // Header Font Style
        XSSFFont bodyFont = (XSSFFont) workbook.createFont();
        bodyFont.setColor(new XSSFColor(new byte[] { (byte) 255, (byte) 255, (byte) 255 }));

        // Body Cell Style
        XSSFCellStyle bodyStyle = (XSSFCellStyle) workbook.createCellStyle();
        bodyStyle.setBorderLeft(BorderStyle.THIN);
        bodyStyle.setBorderRight(BorderStyle.THIN);
        bodyStyle.setBorderTop(BorderStyle.THIN);
        bodyStyle.setBorderBottom(BorderStyle.THIN);
        bodyStyle.setFillForegroundColor(new XSSFColor(new byte[] { 70, 75, 80 }));
        bodyStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        bodyStyle.setFont(bodyFont);
        bodyStyle.setAlignment(HorizontalAlignment.CENTER);
        bodyStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        // Header 생성
        int rowCount = 0;
        String[] headerNames = { "건물 명", "호실 번호" };
        Row headerRow = sheet.createRow(rowCount++);

        for (int i = 0; i < headerNames.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headerNames[i]);
            cell.setCellStyle(headerStyle);
        }

        // Body 데이터 예시
        Integer[][] bodyData = {
                { 101, 201 },
        };

        for (Integer[] rowArr : bodyData) {
            Row row = sheet.createRow(rowCount++);
            for (int i = 0; i < rowArr.length; i++) {
                Cell cell = row.createCell(i);
                cell.setCellValue(rowArr[i]);
                cell.setCellStyle(bodyStyle);
            }
        }

        // 설명
        String[] description = {
                "**2행의 서식에 맞게 작성**",
                "예시"
        };

        XSSFCellStyle alignStyle = (XSSFCellStyle) workbook.createCellStyle();
        alignStyle.setAlignment(HorizontalAlignment.CENTER);
        alignStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        for (int i = 0; i < description.length; i++) {
            Row row = sheet.getRow(i);
            Cell cell = row.createCell(2); // C열
            cell.setCellValue(description[i]);
            cell.setCellStyle(alignStyle);
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

                for (int i = 2; i <= sheet.getLastRowNum(); i++) {
                    Row row = sheet.getRow(i);
                    if (row == null)
                        continue;

                    DataFormatter formatter = new DataFormatter();

                    String dormCode = formatter.formatCellValue(row.getCell(0));
                    String roomNumber = formatter.formatCellValue(row.getCell(1));

                    if (dormCode.isEmpty() || roomNumber.isEmpty())
                        continue;

                    // 1. 기숙사 존재 확인
                    Dorm dorm = dormRepository.findByDormCode(dormCode)
                            .orElseGet(() -> Dorm.builder()
                                    .dormCode(dormCode)
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
