package com.dormclean.dorm_cleaning_management.service;

import com.dormclean.dorm_cleaning_management.dto.qr.QrRequestDto;
import com.dormclean.dorm_cleaning_management.dto.qr.QrResponseDto;

import java.util.List;

public interface QrCodeService {
    byte[] createSecureQr(QrRequestDto dto);

    byte[] generateQrCode(String content, int width, int height, String labelText);

    QrResponseDto getQrData(String token);

    byte[] generateZipForDorms(List<String> dormCodes);
}
