package com.dormclean.dorm_cleaning_management.repository;

import com.dormclean.dorm_cleaning_management.entity.CleaningCode;
import com.dormclean.dorm_cleaning_management.entity.Dorm;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CleaningCodeRepository extends JpaRepository<CleaningCode, Long> {
    Optional<CleaningCode> findByCode(String cleaningCode);
    Optional<CleaningCode> findByDorm(Dorm dorm);
}
