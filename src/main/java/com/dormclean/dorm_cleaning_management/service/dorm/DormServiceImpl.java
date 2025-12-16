package com.dormclean.dorm_cleaning_management.service.dorm;

import com.dormclean.dorm_cleaning_management.dto.dorm.CreateDormRequestDto;
import com.dormclean.dorm_cleaning_management.dto.dorm.DormDeleteRequestDto;
import com.dormclean.dorm_cleaning_management.dto.dorm.DormListResponseDto;
import com.dormclean.dorm_cleaning_management.entity.Dorm;
import com.dormclean.dorm_cleaning_management.repository.DormRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DormServiceImpl implements DormService {

    private final DormRepository dormRepository;

    public List<DormListResponseDto> findAllDorms() {
        return dormRepository.findAll().stream()
                .map(d -> new DormListResponseDto(d.getDormCode()))
                .toList();
    }

    public Dorm createDorm(CreateDormRequestDto dto) {
        Dorm dorm = Dorm.builder()
                .dormCode(dto.dormCode())
                .build();

        return dormRepository.save(dorm);
    }

    @Transactional
    public void deleteDorm(DormDeleteRequestDto dto) {
        Dorm dorm = dormRepository.findByDormCode(dto.dormCode())
                .orElseThrow(() -> new IllegalArgumentException("해당 생활관을 찾을 수 없습니다."));

        dormRepository.delete(dorm);
    }
}
