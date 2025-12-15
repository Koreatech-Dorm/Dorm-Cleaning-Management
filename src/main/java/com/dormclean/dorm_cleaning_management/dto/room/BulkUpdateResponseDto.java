package com.dormclean.dorm_cleaning_management.dto.room;

import java.time.Instant;

public record BulkUpdateResponseDto(
                int updatedCount,
                Instant time) {
}
