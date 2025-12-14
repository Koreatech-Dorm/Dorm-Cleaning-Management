package com.dormclean.dorm_cleaning_management.dto.cleaning;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegistrationCleaningCodeRequestDto(
        @NotBlank(message = "청소 코드를 입력해주세요.")
        @Size(min = 4, max = 10, message = "청소 코드는 4자 이상 10자 이하로 입력해주세요.")
        @Pattern(
                regexp = "^[A-Za-z0-9]+$",
                message = "청소 코드는 영어와 숫자만 입력 가능합니다."
        )
        String cleaningCode) {
}
