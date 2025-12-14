package com.dormclean.dorm_cleaning_management.dto.zipFile;

public record ZipFileEntry(
        String fileName,     // ex) 생활관A/QR_A동_101호.png
        byte[] imageBytes    // 실제 생성된 QR 코드 이미지 데이터
) {}
