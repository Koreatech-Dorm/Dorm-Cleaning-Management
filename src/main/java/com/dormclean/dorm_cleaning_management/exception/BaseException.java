package com.dormclean.dorm_cleaning_management.exception;

import com.dormclean.dorm_cleaning_management.entity.enums.ErrorCode;

import lombok.Getter;

@Getter
public abstract class BaseException extends RuntimeException {

    private final ErrorCode errorCode;

    protected BaseException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
