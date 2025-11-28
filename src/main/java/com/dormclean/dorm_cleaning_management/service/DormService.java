package com.dormclean.dorm_cleaning_management.service;

import com.dormclean.dorm_cleaning_management.entity.Dorm;
import com.dormclean.dorm_cleaning_management.repository.DormRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DormService {

    private final DormRepository dormRepository;

    public List<Dorm> findAllDorms() {
        return dormRepository.findAll();
    }

    public Dorm createDorm(String dormCode, String name) {
        Dorm dorm = Dorm.builder()
                .dormCode(dormCode)
                .dormName(name)
                .build();

        return dormRepository.save(dorm);
    }

    @Transactional
    public void updateDorm(String dormCode, String newName) {
        Dorm dorm = dormRepository.findByDormCode(dormCode)
                .orElseThrow(() -> new IllegalAccessError("해당 생활관을 찾을 수 없습니다."));
        dorm.setDormName(newName);
    }

    @Transactional
    public void deleteDorm(String dormCode) {
        Dorm dorm = dormRepository.findByDormCode(dormCode)
                .orElseThrow(() -> new IllegalArgumentException("해당 생활관을 찾을 수 없습니다."));

        dormRepository.delete(dorm);
    }
}
