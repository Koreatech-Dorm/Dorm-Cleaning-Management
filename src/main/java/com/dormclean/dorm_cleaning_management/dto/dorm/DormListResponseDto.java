package com.dormclean.dorm_cleaning_management.dto.dorm;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record DormListResponseDto(
        String dormCode) {
}
