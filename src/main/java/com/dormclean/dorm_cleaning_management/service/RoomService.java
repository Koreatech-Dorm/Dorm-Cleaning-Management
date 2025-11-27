package com.dormclean.dorm_cleaning_management.service;

import com.dormclean.dorm_cleaning_management.entity.Dorm;
import com.dormclean.dorm_cleaning_management.entity.Room;
import com.dormclean.dorm_cleaning_management.repository.DormRepository;
import com.dormclean.dorm_cleaning_management.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final DormRepository dormRepository;

    // 1️⃣ 방 생성
    public Room createRoom(String dormCode, Integer floor, String roomNumber) {
        Dorm dorm = dormRepository.findByDormCode(dormCode);
        if (dorm == null) {
            throw new IllegalArgumentException("해당 dormCode의 기숙사가 없습니다.");
        }

        Room room = Room.builder()
                .dorm(dorm)
                .floor(floor)
                .roomNumber(roomNumber)
                .status(Room.RoomStatus.OCCUPIED)
                .build();

        return roomRepository.save(room);
    }

    // 2️⃣ 특정 Dorm + Floor의 방 목록 조회
    public List<Room> getRoomsByDormAndFloor(Dorm dorm, Integer floor) {
        return roomRepository.findBydormAndFloor(dorm, floor);
    }

    // 3️⃣ 특정 Dorm의 모든 방 조회 (층 목록 등)
    public List<Room> getRoomsByDorm(Dorm dorm) {
        return roomRepository.findBydorm(dorm);
    }

    // 4️⃣ 방 상태 변경
    @Transactional
    public void updateRoomStatus(Long roomId, Room.RoomStatus newStatus) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("방을 찾을 수 없습니다. ID=" + roomId));
        room.setStatus(newStatus);
        // @Transactional이면 save 안 해도 변경 자동 반영
    }
}
