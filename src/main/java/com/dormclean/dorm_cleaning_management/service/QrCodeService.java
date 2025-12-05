package com.dormclean.dorm_cleaning_management.service;

import com.dormclean.dorm_cleaning_management.dto.QrRequestDto;
import com.dormclean.dorm_cleaning_management.dto.QrResponseDto;

public interface QrCodeService {
    byte[] createSecureQr(QrRequestDto dto);

    byte[] generateQrCode(String content, int width, int height);

    QrResponseDto getQrData(String token);
}
