package com.dormclean.dorm_cleaning_management.exception.admin;

import com.dormclean.dorm_cleaning_management.entity.enums.ErrorCode;
import com.dormclean.dorm_cleaning_management.exception.BaseException;

public class AdminAlreadyExistsException extends BaseException {

    public AdminAlreadyExistsException() {
        super(ErrorCode.ADMIN_ALREADY_EXISTS);
    }
}