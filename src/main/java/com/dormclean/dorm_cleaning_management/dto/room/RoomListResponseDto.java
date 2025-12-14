package com.dormclean.dorm_cleaning_management.dto.room;

import java.time.Instant;

public record RoomListResponseDto(
        String dormCode,
        Integer floor,
        String roomNumber,
        String status,
        Instant cleanedAt,
        Instant checkInAt,
        Instant checkOutAt) {
}
