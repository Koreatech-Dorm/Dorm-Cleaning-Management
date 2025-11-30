package com.dormclean.dorm_cleaning_management.service;

import com.dormclean.dorm_cleaning_management.dto.CleaningCodeDto;

public interface CleaningCodeService {
    public void registration(String dormCode, String cleaningCode);
    public void useCleaningCode(String cleaningCode, String dormCode);
}
