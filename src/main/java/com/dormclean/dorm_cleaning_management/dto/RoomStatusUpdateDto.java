package com.dormclean.dorm_cleaning_management.dto;

import com.dormclean.dorm_cleaning_management.entity.Room;

public record RoomStatusUpdateDto(
        Room.RoomStatus status) {
}
