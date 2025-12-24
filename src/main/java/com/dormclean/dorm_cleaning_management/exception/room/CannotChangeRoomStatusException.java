package com.dormclean.dorm_cleaning_management.exception.room;

import com.dormclean.dorm_cleaning_management.entity.enums.ErrorCode;
import com.dormclean.dorm_cleaning_management.exception.BaseException;

public class CannotChangeRoomStatusException extends BaseException {

    public CannotChangeRoomStatusException() {
        super(ErrorCode.CANNOT_CHANGE_ROOM_STATUS);
    }
}