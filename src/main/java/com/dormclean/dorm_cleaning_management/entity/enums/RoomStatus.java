package com.dormclean.dorm_cleaning_management.entity.enums;

public enum RoomStatus {
    NOT_USED, // 입실 이전 공실 상태
    OCCUPIED, // 재실 상태
    VACANT_DIRTY, // 퇴실 후 상태
    VACANT_CLEAN, // 청소 후 상태
}