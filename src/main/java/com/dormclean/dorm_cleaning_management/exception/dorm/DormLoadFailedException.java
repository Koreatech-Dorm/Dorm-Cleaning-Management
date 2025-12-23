package com.dormclean.dorm_cleaning_management.exception.dorm;

import com.dormclean.dorm_cleaning_management.entity.enums.ErrorCode;
import com.dormclean.dorm_cleaning_management.exception.BaseException;

public class DormLoadFailedException extends BaseException {

    public DormLoadFailedException() {
        super(ErrorCode.DORM_LOAD_FAILED);
    }
}