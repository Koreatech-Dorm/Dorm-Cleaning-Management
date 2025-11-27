package com.dormclean.dorm_cleaning_management.repository;

import com.dormclean.dorm_cleaning_management.entity.enums.CleanStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dormclean.dorm_cleaning_management.entity.Room;
import com.dormclean.dorm_cleaning_management.entity.Dorm;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    // 특정 방 조회
    Room findByRoomNumber(String roomNumber);

    // 특정 기숙사의 전체 방 조회
    List<Room> findBydorm(Dorm dorm);

    // 층별 조회
    List<Room> findBydormAndFloor(Dorm dorm, Integer floor);

    // 특정 기숙사의 방 조회
    Optional<Room> findByDormAndRoomNumber(Dorm dorm, String roomNumber);

    // 청소 상태로 조회
    List<Room> findByCleanStatus(CleanStatus status);
}
