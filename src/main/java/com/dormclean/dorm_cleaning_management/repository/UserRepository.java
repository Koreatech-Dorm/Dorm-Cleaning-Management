package com.dormclean.dorm_cleaning_management.repository;

import com.dormclean.dorm_cleaning_management.entity.AdminUser;
import com.dormclean.dorm_cleaning_management.entity.enums.UserRole;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<AdminUser, Long> {
    Optional<AdminUser> findByUsername(String username);

    // 특정 권한을 가진 단일 사용자 조회
    Optional<AdminUser> findByRole(UserRole role);

    // 특정 권한을 가진 모든 사용자 조회
    List<AdminUser> findAllByRole(UserRole role);
}
