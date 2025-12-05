package com.dormclean.dorm_cleaning_management.service;

import com.dormclean.dorm_cleaning_management.entity.Dorm;
import com.dormclean.dorm_cleaning_management.entity.Room;
import com.dormclean.dorm_cleaning_management.entity.enums.RoomStatus;
import com.dormclean.dorm_cleaning_management.repository.DormRepository;
import com.dormclean.dorm_cleaning_management.repository.RoomRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;
    private final DormRepository dormRepository;

    // 방 생성
    @Override
    public Room createRoom(String dormCode, Integer floor, String roomNumber) {
        Dorm dorm = dormRepository.findByDormCode(dormCode)
                .orElseThrow(() -> new IllegalArgumentException("해당 dormCode의 기숙사가 없습니다."));

        Room room = Room.builder()
                .dorm(dorm)
                .floor(floor)
                .roomNumber(roomNumber)
                .status(RoomStatus.READY)
                .build();

        return roomRepository.save(room);
    }

    // 특정 Dorm + Floor의 방 목록 조회
    @Override
    public List<Room> getRoomsByDormAndFloor(Dorm dorm, Integer floor) {
        return roomRepository.findBydormAndFloor(dorm, floor);
    }

    // 특정 Dorm의 모든 방 조회 (층 목록 등)
    @Override
    public List<Room> getRoomsByDorm(Dorm dorm) {
        return roomRepository.findByDorm(dorm);
    }

    // 호실 상태 변경
    @Override
    @Transactional
    public void updateRoomStatus(String dormCode, String roomNumber, String newRoomStatus) {
        Dorm dorm = dormRepository.findByDormCode(dormCode)
                .orElseThrow(() -> new IllegalArgumentException("해당 생활관을 찾을 수 없습니다."));
        Room room = roomRepository.findByDormAndRoomNumber(dorm, roomNumber)
                .orElseThrow(() -> new IllegalArgumentException("해당 호실을 찾을 수 없습니다."));

        if (newRoomStatus.equals("OCCUPIED"))
            room.updateStatus(RoomStatus.OCCUPIED);
        else if (newRoomStatus.equals("READY"))
            room.updateStatus(RoomStatus.READY);
        else
            room.updateStatus(RoomStatus.VACANT_DIRTY);
    }

    // 호실 삭제
    @Override
    @Transactional
    public void deleteRoom(String dormCode, String roomNumber) {
        Dorm dorm = dormRepository.findByDormCode(dormCode)
                .orElseThrow(() -> new IllegalArgumentException("해당 상활관을 찾을 수 없습니다."));
        Room room = roomRepository.findByDormAndRoomNumber(dorm, roomNumber)
                .orElseThrow(() -> new IllegalArgumentException("해당 호실을 찾을 수 없습니다."));

        roomRepository.delete(room);
    }
}
