package com.dormclean.dorm_cleaning_management.controller;

import com.dormclean.dorm_cleaning_management.dto.qr.QrRequestDto;
import com.dormclean.dorm_cleaning_management.service.qr.QrCodeService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/api")
@RequiredArgsConstructor
@Tag(name = "QR 코드 API", description = "인증용 보안 QR 코드 생성 API")
public class QrCodeController {
    private final QrCodeService qrCodeService;

    @GetMapping("/qr/generate")
    public ResponseEntity<byte[]> generateQrCode(@Valid @ModelAttribute QrRequestDto dto) {
        try {
            byte[] qrImage = qrCodeService.createSecureQr(dto);

            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .body(qrImage);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/qr/generate/zip")
    public ResponseEntity<byte[]> generateQrCodeZip(@RequestParam("dormCodes") List<String> dormCodes) {
        byte[] zipFile = qrCodeService.generateZipForDorms(dormCodes);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=dorm_qr_codes.zip")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(zipFile);
    }
}
