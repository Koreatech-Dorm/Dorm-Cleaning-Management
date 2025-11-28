package com.dormclean.dorm_cleaning_management.service;

import com.dormclean.dorm_cleaning_management.entity.CleaningCode;
import com.dormclean.dorm_cleaning_management.entity.Dorm;
import com.dormclean.dorm_cleaning_management.repository.CleaningCodeRepository;
import com.dormclean.dorm_cleaning_management.repository.DormRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CleaningCodeServiceImpl implements  CleaningCodeService {
    private final CleaningCodeRepository cleaningCodeRepository;
    private final DormRepository dormRepository;

    @Override
    public void registration(String dormCode, String cleaningCode) {
        Dorm dorm = dormRepository.findByDormCode(dormCode).orElseThrow(() -> new IllegalArgumentException("해당 기숙사가 존재하지 않습니다."));
        CleaningCode cleaningcode = CleaningCode.builder()
                .dorm(dorm)
                .code(cleaningCode)
                .build();

        cleaningCodeRepository.save(cleaningcode);
    }
}
