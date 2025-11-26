package com.dormclean.dorm_cleaning_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dormclean.dorm_cleaning_management.entity.Dorm;

@Repository
public interface DormRepository extends JpaRepository<Dorm, Long> {
    Dorm findByDormCode(String dormCode);
}
