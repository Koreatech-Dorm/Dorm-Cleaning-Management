package com.dormclean.dorm_cleaning_management.controller;

import com.dormclean.dorm_cleaning_management.service.ExcelService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "Excel 관리 API", description = "Excel 관련 API")
public class ExcelController {

    private final ExcelService excelService;

    // 호실 등록 양식 다운로드
    @GetMapping("/Excel/download")
    public ResponseEntity<Void> download(HttpServletResponse res) throws Exception {

        // 파일 이름 설정
        String fileName = "Room_Information";

        res.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        res.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        excelService.downloadExcel(res);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/Excel/register")
    public ResponseEntity<?> uploadRooms(
            @RequestParam("file") MultipartFile file) {

        try {
            excelService.registerByExcel(file);
            return ResponseEntity.ok("Success");

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Excel parsing error: " + e.getMessage());
        }
    }
}
