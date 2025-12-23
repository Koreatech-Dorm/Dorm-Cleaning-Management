package com.dormclean.dorm_cleaning_management.exception.room;

import com.dormclean.dorm_cleaning_management.entity.enums.ErrorCode;
import com.dormclean.dorm_cleaning_management.exception.BaseException;

public class RoomNotFoundException extends BaseException {

    public RoomNotFoundException() {
        super(ErrorCode.ROOM_NOT_FOUND);
    }
}