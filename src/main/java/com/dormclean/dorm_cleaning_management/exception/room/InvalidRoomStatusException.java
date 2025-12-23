package com.dormclean.dorm_cleaning_management.exception.room;

import com.dormclean.dorm_cleaning_management.entity.enums.ErrorCode;
import com.dormclean.dorm_cleaning_management.exception.BaseException;

public class InvalidRoomStatusException extends BaseException {

    public InvalidRoomStatusException() {
        super(ErrorCode.INVALID_ROOM_STATUS);
    }
}