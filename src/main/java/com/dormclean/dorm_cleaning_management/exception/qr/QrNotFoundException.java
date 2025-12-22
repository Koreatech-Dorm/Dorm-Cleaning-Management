package com.dormclean.dorm_cleaning_management.exception.qr;

import com.dormclean.dorm_cleaning_management.entity.enums.ErrorCode;
import com.dormclean.dorm_cleaning_management.exception.BaseException;

public class QrNotFoundException extends BaseException {

    public QrNotFoundException() {
        super(ErrorCode.QR_NOT_FOUND);
    }
}