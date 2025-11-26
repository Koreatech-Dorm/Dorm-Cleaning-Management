package com.dormclean.dorm_cleaning_management.controller;

import com.dormclean.dorm_cleaning_management.dto.QrRequestDto;
import com.dormclean.dorm_cleaning_management.service.QrCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequiredArgsConstructor
public class QrCodeController {
    private final QrCodeService qrCodeService;

    @Value("${app.host}")
    private String host;

    @GetMapping("/api/qr/generate")
    public ResponseEntity<byte[]> generateQrCode(@ModelAttribute QrRequestDto dto) {
        try{
            String baseUrl = String.format("%s/check", host);

            // 받은 요청
            String dormName = dto.dormName();
            String roomName = dto.roomName();

            // 한글 사용을 위한 encode
            String encodedDorm = URLEncoder.encode(dormName, StandardCharsets.UTF_8);
            String encodedRoom = URLEncoder.encode(roomName, StandardCharsets.UTF_8);

            String content = String.format("%s?dorm=%s&room=%s", baseUrl, encodedDorm, encodedRoom);

            byte[] qrImage = qrCodeService.generateQrCode(content, 250, 250);

            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .body(qrImage);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
}
