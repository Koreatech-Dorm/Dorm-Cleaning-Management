package com.dormclean.dorm_cleaning_management.service;

import com.dormclean.dorm_cleaning_management.entity.Dorm;
import com.dormclean.dorm_cleaning_management.repository.DormRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DormService {

    private final DormRepository dormRepository;

    public Dorm createDorm(String code, String name) {
        Dorm dorm = Dorm.builder()
                .dormCode(code)
                .dormName(name)
                .build();

        return dormRepository.save(dorm);
    }
}
