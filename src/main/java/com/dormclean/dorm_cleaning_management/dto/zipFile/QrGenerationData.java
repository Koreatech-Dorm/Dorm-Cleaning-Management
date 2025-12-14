package com.dormclean.dorm_cleaning_management.dto.zipFile;

public record QrGenerationData(
        String content,    // QR 코드에 들어갈 URL (예: https://host/check?token=...)
        String labelText,  // 이미지 하단에 적힐 텍스트 (ex) A동 101호)
        String fileName    // ZIP 파일 내의 경로 및 파일명
) {}