package com.dormclean.dorm_cleaning_management.exception.qr;

import com.dormclean.dorm_cleaning_management.entity.enums.ErrorCode;
import com.dormclean.dorm_cleaning_management.exception.BaseException;

public class InvalidQrException extends BaseException {

    public InvalidQrException() {
        super(ErrorCode.INVALID_QR);
    }
}