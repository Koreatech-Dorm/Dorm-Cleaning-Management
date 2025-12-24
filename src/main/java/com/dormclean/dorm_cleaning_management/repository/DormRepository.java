package com.dormclean.dorm_cleaning_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dormclean.dorm_cleaning_management.entity.Dorm;

import java.util.List;
import java.util.Optional;

@Repository
public interface DormRepository extends JpaRepository<Dorm, Long> {
    boolean existsByDormCode(String dormCode);

    Optional<Dorm> findById(Long roomId);

    Optional<Dorm> findByDormCode(String dormCode);

    List<Dorm> findAllByOrderByDormCodeAsc();
}
