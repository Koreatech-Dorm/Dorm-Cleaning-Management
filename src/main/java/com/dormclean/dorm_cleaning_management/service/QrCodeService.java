package com.dormclean.dorm_cleaning_management.service;

import com.dormclean.dorm_cleaning_management.dto.QrRequestDto;
import com.dormclean.dorm_cleaning_management.dto.QrResponseDto;

public interface QrCodeService {
    public byte[] createSecureQr(QrRequestDto dto);

    public byte[] generateQrCode(String content, int width, int height);

    public QrResponseDto getQrData(String token);
}
