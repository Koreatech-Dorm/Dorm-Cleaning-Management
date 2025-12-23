package com.dormclean.dorm_cleaning_management.exception.room;

import com.dormclean.dorm_cleaning_management.entity.enums.ErrorCode;
import com.dormclean.dorm_cleaning_management.exception.BaseException;

public class RoomAlreadyExistsException extends BaseException {

    public RoomAlreadyExistsException() {
        super(ErrorCode.ROOM_ALREADY_EXISTS);
    }
}