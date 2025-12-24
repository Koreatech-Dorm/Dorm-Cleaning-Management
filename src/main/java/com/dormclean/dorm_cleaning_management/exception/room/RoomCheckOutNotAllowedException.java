package com.dormclean.dorm_cleaning_management.exception.room;

import com.dormclean.dorm_cleaning_management.entity.enums.ErrorCode;
import com.dormclean.dorm_cleaning_management.exception.BaseException;

public class RoomCheckOutNotAllowedException extends BaseException {

    public RoomCheckOutNotAllowedException() {
        super(ErrorCode.ROOM_CHECK_OUT_NOT_ALLOWED);
    }
}