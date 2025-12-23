package com.dormclean.dorm_cleaning_management.exception.excel;

import com.dormclean.dorm_cleaning_management.entity.enums.ErrorCode;
import com.dormclean.dorm_cleaning_management.exception.BaseException;

public class ExcelProcessingFailedException extends BaseException {

    public ExcelProcessingFailedException() {
        super(ErrorCode.DORM_ALREADY_EXISTS);
    }
}