package com.dormclean.dorm_cleaning_management.controller;

import com.dormclean.dorm_cleaning_management.dto.qr.QrRequestDto;
import com.dormclean.dorm_cleaning_management.service.qr.QrCodeService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

@RestController
@RequestMapping("/admin/api")
@RequiredArgsConstructor
@Tag(name = "QR 코드 API", description = "인증용 보안 QR 코드 생성 API")
public class QrCodeController {
    private final QrCodeService qrCodeService;

    @GetMapping("/qr/generate")
    public ResponseEntity<byte[]> generateQrCode(@Valid @ModelAttribute QrRequestDto dto) {
        byte[] qrImage = qrCodeService.createSecureQr(dto);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(qrImage);
    }

    @GetMapping("/qr/generate/zip")
    public void generateQrCodeZip(
            @RequestParam("dormCodes") List<String> dormCodes,
            HttpServletResponse response) throws IOException {
        response.setContentType("application/zip");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=dorm_qr_codes.zip");

        // 서비스에서 스트림에 직접 데이터를 쏘도록 호출
        qrCodeService.generateZipForDormsToStream(dormCodes, response.getOutputStream());
    }
}
