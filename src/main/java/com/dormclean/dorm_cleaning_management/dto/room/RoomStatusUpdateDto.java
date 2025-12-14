package com.dormclean.dorm_cleaning_management.dto.room;

import jakarta.validation.constraints.NotBlank;

public record RoomStatusUpdateDto(
        @NotBlank(message = "생활관 코드는 필수입니다.")
        String dormCode,
        @NotBlank(message = "새로운 방 상태는 필수입니다.")
        String newRoomStatus) {
}
