package com.dormclean.dorm_cleaning_management.dto;

public record AdminLoginRequestDto(
        String userId,
        String password
) {
}
