package com.dormclean.dorm_cleaning_management.dto;

import com.dormclean.dorm_cleaning_management.entity.Room;

public record RoomDto(
        Long id,
        String number,
        Room.RoomStatus status,
        String statusLabel) {
}
