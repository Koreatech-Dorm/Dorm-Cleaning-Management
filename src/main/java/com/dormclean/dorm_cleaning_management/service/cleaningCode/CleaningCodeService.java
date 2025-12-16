package com.dormclean.dorm_cleaning_management.service.cleaningCode;

import com.dormclean.dorm_cleaning_management.dto.cleaningCode.GetCleaningCodeResponseDto;
import com.dormclean.dorm_cleaning_management.dto.cleaningCode.RegistrationCleaningCodeRequestDto;

public interface CleaningCodeService {
    void registration(RegistrationCleaningCodeRequestDto dto);

    GetCleaningCodeResponseDto getCleaningCode();
}
