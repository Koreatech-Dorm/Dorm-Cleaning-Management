package com.dormclean.dorm_cleaning_management.service;

import com.dormclean.dorm_cleaning_management.dto.CreateDormRequestDto;
import com.dormclean.dorm_cleaning_management.dto.DormDeleteRequestDto;
import com.dormclean.dorm_cleaning_management.dto.DormUpdateRequestDto;
import com.dormclean.dorm_cleaning_management.entity.Dorm;

public interface DormService {
    Dorm createDorm(CreateDormRequestDto dto);

    void updateDorm(DormUpdateRequestDto dto);

    void deleteDorm(DormDeleteRequestDto dto);
}
