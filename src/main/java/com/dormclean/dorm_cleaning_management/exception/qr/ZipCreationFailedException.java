package com.dormclean.dorm_cleaning_management.exception.qr;

import com.dormclean.dorm_cleaning_management.entity.enums.ErrorCode;
import com.dormclean.dorm_cleaning_management.exception.BaseException;

public class ZipCreationFailedException extends BaseException {

    public ZipCreationFailedException() {
        super(ErrorCode.ZIP_CREATION_FAILED);
    }
}