package com.dormclean.dorm_cleaning_management.exception.cleaningCode;

import com.dormclean.dorm_cleaning_management.entity.enums.ErrorCode;
import com.dormclean.dorm_cleaning_management.exception.BaseException;

public class InvalidCleaningCodeException extends BaseException {

    public InvalidCleaningCodeException() {
        super(ErrorCode.INVALID_CLEANING_CODE);
    }
}