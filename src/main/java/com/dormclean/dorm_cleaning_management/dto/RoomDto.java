package com.dormclean.dorm_cleaning_management.dto;

import com.dormclean.dorm_cleaning_management.entity.enums.RoomStatus;

public record RoomDto(
        Long id,
        String number,
        RoomStatus roomStatus,
        String statusLabel) {
}
