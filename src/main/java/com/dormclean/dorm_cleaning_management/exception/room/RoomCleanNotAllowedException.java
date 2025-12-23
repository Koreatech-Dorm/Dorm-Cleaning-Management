package com.dormclean.dorm_cleaning_management.exception.room;

import com.dormclean.dorm_cleaning_management.entity.enums.ErrorCode;
import com.dormclean.dorm_cleaning_management.exception.BaseException;

public class RoomCleanNotAllowedException extends BaseException {

    public RoomCleanNotAllowedException() {
        super(ErrorCode.ROOM_CLEAN_NOT_ALLOWED);
    }
}