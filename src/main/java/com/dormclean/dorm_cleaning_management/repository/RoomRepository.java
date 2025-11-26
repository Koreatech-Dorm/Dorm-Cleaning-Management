package com.dormclean.dorm_cleaning_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dormclean.dorm_cleaning_management.entity.Room;
import com.dormclean.dorm_cleaning_management.entity.Dorm;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    // 특정 기숙사의 전체 방 조회
    List<Room> findByDormitory(Dorm dormitory);

    // 층별 조회
    List<Room> findByDormitoryAndFloor(Dorm dormitory, Integer floor);

    // 방번호로 조회
    Room findByDormitoryAndRoomNumber(Dorm dormitory, String roomNumber);

    // 상태로 조회
    List<Room> findByStatus(Room.RoomStatus status);
}
