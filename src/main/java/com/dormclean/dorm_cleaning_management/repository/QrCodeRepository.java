package com.dormclean.dorm_cleaning_management.repository;

import com.dormclean.dorm_cleaning_management.entity.QrCode;
import com.dormclean.dorm_cleaning_management.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface QrCodeRepository extends JpaRepository<QrCode, Long> {
    Optional<QrCode> findByRoom(Room room);
    Optional<QrCode> findByUuid(String uuid);
}