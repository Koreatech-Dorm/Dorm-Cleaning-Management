package com.dormclean.dorm_cleaning_management.service;

import com.dormclean.dorm_cleaning_management.dto.cleaning.GetCleaningCodeResponseDto;
import com.dormclean.dorm_cleaning_management.dto.cleaning.RegistrationCleaningCodeRequestDto;

public interface CleaningCodeService {
    void registration(RegistrationCleaningCodeRequestDto dto);

    GetCleaningCodeResponseDto getCleaningCode();
}
