package com.dormclean.dorm_cleaning_management.dto;

public record RoomListResponseDto(
        String dormCode,
        Integer floor,
        String roomNumber,
        String status) {
}
