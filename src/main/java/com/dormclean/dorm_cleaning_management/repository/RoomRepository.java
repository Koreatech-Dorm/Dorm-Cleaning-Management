package com.dormclean.dorm_cleaning_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dormclean.dorm_cleaning_management.entity.Room;
import com.dormclean.dorm_cleaning_management.entity.Dorm;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    // 호실 생성 시: 특정 생활관의 호실 중복 체크
    boolean existsByDormAndRoomNumber(Dorm dorm, String roomNumber);

    // 특정 방 조회
    Optional<Room> findById(Long roomId);

    // 특정 기숙사의 전체 방 조회
    List<Room> findByDorm(Dorm dorm);

    // 층별 조회
    List<Room> findBydormAndFloor(Dorm dorm, Integer floor);

    // 특정 기숙사의 방 조회
    Optional<Room> findByDormAndRoomNumber(Dorm dorm, String roomNumber);
}
