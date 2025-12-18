package com.dormclean.dorm_cleaning_management.dto.qr;

import com.dormclean.dorm_cleaning_management.entity.enums.RoomStatus;

public record QrResponseDto(
                String dormCode,
                String roomNumber,
                RoomStatus roomStatus) {
}
