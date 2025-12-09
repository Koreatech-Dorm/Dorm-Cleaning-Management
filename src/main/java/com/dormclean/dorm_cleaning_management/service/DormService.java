package com.dormclean.dorm_cleaning_management.service;

import com.dormclean.dorm_cleaning_management.dto.CreateDormRequestDto;
import com.dormclean.dorm_cleaning_management.dto.DormDeleteRequestDto;
import com.dormclean.dorm_cleaning_management.dto.DormListResponseDto;
import com.dormclean.dorm_cleaning_management.dto.DormUpdateRequestDto;
import com.dormclean.dorm_cleaning_management.entity.Dorm;

import java.util.List;

public interface DormService {
    List<DormListResponseDto> findAllDorms();

    Dorm createDorm(CreateDormRequestDto dto);

    void deleteDorm(DormDeleteRequestDto dto);
}
