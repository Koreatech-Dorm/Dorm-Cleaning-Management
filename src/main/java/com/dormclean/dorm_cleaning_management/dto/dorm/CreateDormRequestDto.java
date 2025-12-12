package com.dormclean.dorm_cleaning_management.dto.dorm;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateDormRequestDto(
        @Pattern(regexp = "^[0-9]+$", message = "생활관 코드는 숫자만 입력 가능합니다.")
        @Size(min = 1, max = 3, message = "생활관 코드는 3자리 숫자여야 합니다.")
        String dormCode) {
}
