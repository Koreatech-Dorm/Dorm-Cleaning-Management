package com.dormclean.dorm_cleaning_management.service.admin;

import com.dormclean.dorm_cleaning_management.dto.admin.AdminLoginRequestDto;
import com.dormclean.dorm_cleaning_management.dto.admin.AdminLoginResponseDto;

public interface AuthService {
    AdminLoginResponseDto login(AdminLoginRequestDto dto);
}
