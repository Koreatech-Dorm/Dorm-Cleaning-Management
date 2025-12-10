package com.dormclean.dorm_cleaning_management.service;

import com.dormclean.dorm_cleaning_management.dto.AdminLoginRequestDto;
import com.dormclean.dorm_cleaning_management.dto.AdminLoginResponseDto;

public interface AuthService {
    AdminLoginResponseDto login(AdminLoginRequestDto dto);
}
