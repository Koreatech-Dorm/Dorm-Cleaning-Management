package com.dormclean.dorm_cleaning_management.service;

import com.dormclean.dorm_cleaning_management.dto.check.CheckRequestDto;
import com.dormclean.dorm_cleaning_management.dto.cleaning.CleaningCodeDto;

public interface CheckService {
    void checkIn(CheckRequestDto dto);

    void checkOut(CheckRequestDto dto);

    void cleanCheck(CheckRequestDto dto);

    void useCleaningCode(CleaningCodeDto dto);
}
