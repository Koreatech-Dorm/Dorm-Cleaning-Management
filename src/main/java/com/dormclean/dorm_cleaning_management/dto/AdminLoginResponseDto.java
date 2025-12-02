package com.dormclean.dorm_cleaning_management.dto;

public record AdminLoginResponseDto(
        String accessToken,
        String refreshToken
) {
}
