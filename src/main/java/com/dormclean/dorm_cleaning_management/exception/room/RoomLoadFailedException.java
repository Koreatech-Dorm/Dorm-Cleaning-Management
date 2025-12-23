package com.dormclean.dorm_cleaning_management.exception.room;

import com.dormclean.dorm_cleaning_management.entity.enums.ErrorCode;
import com.dormclean.dorm_cleaning_management.exception.BaseException;

public class RoomLoadFailedException extends BaseException {

    public RoomLoadFailedException() {
        super(ErrorCode.ROOM_LOAD_FAILED);
    }
}