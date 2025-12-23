package com.dormclean.dorm_cleaning_management.service.dorm;

import com.dormclean.dorm_cleaning_management.dto.dorm.CreateDormRequestDto;
import com.dormclean.dorm_cleaning_management.dto.dorm.DormDeleteRequestDto;
import com.dormclean.dorm_cleaning_management.dto.dorm.DormListResponseDto;
import com.dormclean.dorm_cleaning_management.entity.Dorm;
import com.dormclean.dorm_cleaning_management.exception.dorm.DormAlreadyExistsException;
import com.dormclean.dorm_cleaning_management.exception.dorm.DormNotFoundException;
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
        if (dormRepository.existsByDormCode(dto.dormCode())) {
            throw new DormAlreadyExistsException();
        }
        Dorm dorm = Dorm.builder()
                .dormCode(dto.dormCode())
                .build();

        return dormRepository.save(dorm);
    }

    @Transactional
    public void deleteDorm(DormDeleteRequestDto dto) {
        Dorm dorm = dormRepository.findByDormCode(dto.dormCode())
                .orElseThrow(DormNotFoundException::new);

        dormRepository.delete(dorm);
    }
}
