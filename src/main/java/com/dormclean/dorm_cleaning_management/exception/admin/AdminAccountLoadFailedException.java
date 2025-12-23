package com.dormclean.dorm_cleaning_management.exception.admin;

import com.dormclean.dorm_cleaning_management.entity.enums.ErrorCode;
import com.dormclean.dorm_cleaning_management.exception.BaseException;

public class AdminAccountLoadFailedException extends BaseException {

    public AdminAccountLoadFailedException() {
        super(ErrorCode.ADMIN_ACCOUNT_LOAD_FAILED);
    }
}