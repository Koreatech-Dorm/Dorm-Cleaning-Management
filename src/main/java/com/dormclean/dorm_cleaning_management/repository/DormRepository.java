package com.dormclean.dorm_cleaning_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dormclean.dorm_cleaning_management.entity.Dorm;

import java.util.Optional;

@Repository
public interface DormRepository extends JpaRepository<Dorm, Long> {
    Optional<Dorm> findByDormCode(String dormCode);
    Optional<Dorm> findByDormName(String dormName);
}
