package com.dormclean.dorm_cleaning_management.dto.qr;

import jakarta.validation.constraints.NotBlank;

public record QrRequestDto(
        @NotBlank(message = "생활관 코드는 필수입니다.")
        String dormCode,
        @NotBlank(message = "호실 번호는 필수입니다.")
        String roomNumber) {
}