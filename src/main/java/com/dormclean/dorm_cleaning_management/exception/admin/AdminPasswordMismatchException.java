package com.dormclean.dorm_cleaning_management.exception.admin;

import com.dormclean.dorm_cleaning_management.entity.enums.ErrorCode;
import com.dormclean.dorm_cleaning_management.exception.BaseException;

public class AdminPasswordMismatchException extends BaseException {

    public AdminPasswordMismatchException() {
        super(ErrorCode.ADMIN_PASSWORD_MISMATCH);
    }
}