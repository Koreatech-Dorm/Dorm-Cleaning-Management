package com.dormclean.dorm_cleaning_management.exception.cleaningCode;

import com.dormclean.dorm_cleaning_management.entity.enums.ErrorCode;
import com.dormclean.dorm_cleaning_management.exception.BaseException;

public class CleaningCodeNotFoundException extends BaseException {

    public CleaningCodeNotFoundException() {
        super(ErrorCode.CLEANING_CODE_NOT_FOUND);
    }
}