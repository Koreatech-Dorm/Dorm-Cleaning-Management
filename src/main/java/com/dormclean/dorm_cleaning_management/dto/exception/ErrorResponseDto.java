package com.dormclean.dorm_cleaning_management.dto.exception;

import com.dormclean.dorm_cleaning_management.exception.BaseException;

public record ErrorResponseDto(
        String errorCode,
        String message,
        String detail) {

    public static ErrorResponseDto from(BaseException e) {
        return new ErrorResponseDto(
                e.getErrorCode().getCode(),
                e.getMessage(),
                null);
    }

    public static ErrorResponseDto from(String errorCode, Exception e) {
        return new ErrorResponseDto(
                errorCode,
                e.getMessage(),
                null);
    }
}
