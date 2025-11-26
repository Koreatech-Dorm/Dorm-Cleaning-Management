package com.dormclean.dorm_cleaning_management.dto;

public record CreateRoomRequestDto(
                String dormCode,
                Integer floor,
                String roomNumber) {
}