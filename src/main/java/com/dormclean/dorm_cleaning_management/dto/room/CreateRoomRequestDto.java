package com.dormclean.dorm_cleaning_management.dto.room;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CreateRoomRequestDto(
        @NotBlank(message = "생활관 코드는 필수입니다.")
        String dormCode,

        @NotBlank(message = "호실 번호는 필수입니다.")
        @Pattern(regexp = "^[0-9]{3,4}[A-Z]?$", message = "호실 번호는 3~4자리 숫자 또는 숫자+알파벳 형식이어야 합니다.")
        String roomNumber
) {
}