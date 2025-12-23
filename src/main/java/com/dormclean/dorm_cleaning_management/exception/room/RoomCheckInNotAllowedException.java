package com.dormclean.dorm_cleaning_management.exception.room;

import com.dormclean.dorm_cleaning_management.entity.enums.ErrorCode;
import com.dormclean.dorm_cleaning_management.exception.BaseException;

public class RoomCheckInNotAllowedException extends BaseException {

    public RoomCheckInNotAllowedException() {
        super(ErrorCode.ROOM_CHECK_IN_NOT_ALLOWED);
    }
}