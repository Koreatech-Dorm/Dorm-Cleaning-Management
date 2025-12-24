package com.dormclean.dorm_cleaning_management.exception.dorm;

import com.dormclean.dorm_cleaning_management.entity.enums.ErrorCode;
import com.dormclean.dorm_cleaning_management.exception.BaseException;

public class DormNotFoundException extends BaseException {

    public DormNotFoundException() {
        super(ErrorCode.DORM_NOT_FOUND);
    }
}