package com.dormclean.dorm_cleaning_management.exception.dorm;

import com.dormclean.dorm_cleaning_management.entity.enums.ErrorCode;
import com.dormclean.dorm_cleaning_management.exception.BaseException;

public class DormAlreadyExistsException extends BaseException {

    public DormAlreadyExistsException() {
        super(ErrorCode.DORM_ALREADY_EXISTS);
    }
}