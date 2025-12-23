package com.dormclean.dorm_cleaning_management.exception.admin;

import com.dormclean.dorm_cleaning_management.entity.enums.ErrorCode;
import com.dormclean.dorm_cleaning_management.exception.BaseException;

public class SuperAdminNotFoundException extends BaseException {

    public SuperAdminNotFoundException() {
        super(ErrorCode.SUPERADMIN_NOT_FOUND);
    }
}