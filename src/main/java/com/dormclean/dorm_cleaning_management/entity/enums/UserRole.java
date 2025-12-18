package com.dormclean.dorm_cleaning_management.entity.enums;

import lombok.Getter;

@Getter
public enum UserRole {
    ADMIN("ROLE_ADMIN"),
    SUPERADMIN("ROLE_SUPERADMIN");

    UserRole(String value) {
        this.value = value;
    }

    private String value;
}
