package com.dormclean.dorm_cleaning_management.exception.room;

import com.dormclean.dorm_cleaning_management.entity.enums.ErrorCode;
import com.dormclean.dorm_cleaning_management.exception.BaseException;

public class FloorLoadFailedException extends BaseException {

    public FloorLoadFailedException() {
        super(ErrorCode.FLOOR_LOAD_FAILED);
    }
}