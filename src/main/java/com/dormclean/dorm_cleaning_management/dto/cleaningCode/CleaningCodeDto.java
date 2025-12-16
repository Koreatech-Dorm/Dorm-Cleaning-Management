package com.dormclean.dorm_cleaning_management.dto.cleaningCode;

import jakarta.validation.constraints.NotBlank;

public record CleaningCodeDto(
                @NotBlank(message = "청소 코드를 입력해주세요.") String cleaningCode) {
}
