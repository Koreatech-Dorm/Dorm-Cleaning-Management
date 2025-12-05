package com.dormclean.dorm_cleaning_management.service;

import com.dormclean.dorm_cleaning_management.dto.CheckRequestDto;
import com.dormclean.dorm_cleaning_management.entity.Dorm;

public interface CheckService {
    void checkIn(CheckRequestDto dto);
    void checkOut(CheckRequestDto dto);
    void cleanCheck(CheckRequestDto dto);
}
