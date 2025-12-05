package com.dormclean.dorm_cleaning_management.service;

import com.dormclean.dorm_cleaning_management.dto.CleaningCodeDto;
import com.dormclean.dorm_cleaning_management.dto.GetCleaningCodeResponseDto;
import com.dormclean.dorm_cleaning_management.dto.RegistrationCleaningCodeRequestDto;

public interface CleaningCodeService {
    void registration(RegistrationCleaningCodeRequestDto dto);

    void useCleaningCode(CleaningCodeDto dto);

    GetCleaningCodeResponseDto getCleaningCode();
}
