package com.dormclean.dorm_cleaning_management.exception.qr;

import com.dormclean.dorm_cleaning_management.entity.enums.ErrorCode;
import com.dormclean.dorm_cleaning_management.exception.BaseException;

public class QrCreationFailedException extends BaseException {

    public QrCreationFailedException() {
        super(ErrorCode.QR_CREATION_FAILED);
    }
}