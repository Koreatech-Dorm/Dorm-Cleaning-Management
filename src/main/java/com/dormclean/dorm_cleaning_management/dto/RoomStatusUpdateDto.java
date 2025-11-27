package com.dormclean.dorm_cleaning_management.dto;

import com.dormclean.dorm_cleaning_management.entity.enums.RoomStatus;

public record RoomStatusUpdateDto(
        RoomStatus roomStatus) {
}
