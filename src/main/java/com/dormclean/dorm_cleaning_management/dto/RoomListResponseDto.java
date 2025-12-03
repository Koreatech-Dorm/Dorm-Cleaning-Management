package com.dormclean.dorm_cleaning_management.dto;

public record RoomListResponseDto(
                Integer floor,
                String roomNumber,
                String status) {
}
