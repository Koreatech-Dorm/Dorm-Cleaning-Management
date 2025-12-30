package com.dormclean.dorm_cleaning_management.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dormclean.dorm_cleaning_management.service.room.ExcelService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/api")
@Tag(name = "Excel 관리 API", description = "Excel 관련 API")
public class ExcelController {

    private final ExcelService excelService;

    // 호실 등록 양식 다운로드
    @GetMapping("/Excel/download")
    public ResponseEntity<ByteArrayResource> download() throws Exception {

        byte[] file = excelService.downloadExcel();
        ByteArrayResource resource = new ByteArrayResource(file);

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=room_template.xlsx")
                .contentLength(file.length)
                .contentType(
                        MediaType.parseMediaType("application/octet-stream"))
                .body(resource);
    }

    @PostMapping("/Excel/register")
    public ResponseEntity<?> uploadRooms(
            @RequestParam("file") MultipartFile file) {

        excelService.registerByExcel(file);
        return ResponseEntity.ok("Success");
    }
}
