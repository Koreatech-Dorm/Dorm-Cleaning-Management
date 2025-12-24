package com.dormclean.dorm_cleaning_management.repository;

import com.dormclean.dorm_cleaning_management.dto.qr.QrResponseDto;
import com.dormclean.dorm_cleaning_management.entity.QrCode;
import com.dormclean.dorm_cleaning_management.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface QrCodeRepository extends JpaRepository<QrCode, Long> {
    Optional<QrCode> findByRoom(Room room);

    @Query("""
    select new com.dormclean.dorm_cleaning_management.dto.qr.QrResponseDto(
        d.dormCode,
        r.roomNumber,
        r.roomStatus
        )
        from QrCode q join q.room r join r.dorm d
        where q.uuid = :token  
    """)
    Optional<QrResponseDto> findByToken(String token);
}