package com.dormclean.dorm_cleaning_management.service.room;

import com.dormclean.dorm_cleaning_management.dto.room.*;
import com.dormclean.dorm_cleaning_management.entity.Dorm;
import com.dormclean.dorm_cleaning_management.entity.Room;
import com.dormclean.dorm_cleaning_management.entity.enums.RoomStatus;
import com.dormclean.dorm_cleaning_management.repository.DormRepository;
import com.dormclean.dorm_cleaning_management.repository.RoomRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {
        private final RoomRepository roomRepository;
        private final DormRepository dormRepository;

        // 방 생성
        @Override
        public Room createRoom(CreateRoomRequestDto dto) {

                Dorm dorm = dormRepository.findByDormCode(dto.dormCode())
                                .orElseThrow(() -> new IllegalArgumentException("해당 생활관이 존재하지 않습니다."));

                Integer floor = extractFloor(dto.roomNumber());

                Room room = Room.builder()
                                .dorm(dorm)
                                .floor(floor)
                                .roomNumber(dto.roomNumber())
                                .status(RoomStatus.READY)
                                .build();

                return roomRepository.save(room);
        }

        private Integer extractFloor(String roomNumber) {
                int cnt = 0, idx = roomNumber.length() - 1;
                while (idx >= 0) {
                        char c = roomNumber.charAt(idx);
                        if (c >= '0' && c <= '9')
                                break;
                        cnt++;
                        idx--;
                }
                int floor = Integer.parseInt(roomNumber.substring(0, roomNumber.length() - (cnt + 2)));

                return floor;
        }

        // 모든 방 조회 (층 목록 등)
        @Override
        @Transactional(readOnly = true)
        public List<RoomListResponseDto> getRooms() {
                return roomRepository.findAllRoomsDto();
        }

        // 특정 Dorm의 모든 방 조회 (층 목록 등)
        @Override
        @Transactional(readOnly = true)
        public List<RoomListResponseDto> getRooms(String dormCode) {
                return roomRepository.findRoomByDormCode(dormCode);
        }

        // 특정 Dorm + Floor의 방 목록 조회
        @Override
        @Transactional(readOnly = true)
        public List<RoomListResponseDto> getRooms(String dormCode, Integer floor) {
                return roomRepository.findRoomByDormCodeAndFloor(dormCode, floor);
        }

        @Override
        public List<Integer> getFloors(String dormCode) {
                Dorm dorm = dormRepository.findByDormCode(dormCode)
                                .orElseThrow(() -> new IllegalArgumentException("Dorm not found"));

                return roomRepository.findByDorm(dorm).stream()
                                .map(Room::getFloor)
                                .distinct()
                                .sorted()
                                .toList();
        }

        // 호실 상태 변경
        @Override
        @Transactional
        public RoomListResponseDto updateRoomStatus(
                        String roomNumber,
                        RoomStatusUpdateDto dto) {

                Dorm dorm = dormRepository.findByDormCode(dto.dormCode())
                                .orElseThrow(() -> new IllegalArgumentException("해당 생활관을 찾을 수 없습니다."));

                Room room = roomRepository.findByDormAndRoomNumber(dorm, roomNumber)
                                .orElseThrow(() -> new IllegalArgumentException("해당 호실을 찾을 수 없습니다."));

                Instant now = Instant.now();

                switch (dto.newRoomStatus()) {
                        case "OCCUPIED" -> {
                                room.updateStatus(RoomStatus.OCCUPIED);
                                room.updateCheckInAt(now);
                        }
                        case "READY" -> {
                                room.updateStatus(RoomStatus.READY);
                                room.updateCleanedAt(now);
                        }
                        case "VACANT" -> {
                                room.updateStatus(RoomStatus.VACANT);
                                room.updateCheckOutAt(now);
                        }
                        default -> throw new IllegalArgumentException("잘못된 상태값");
                }

                return new RoomListResponseDto(
                                dorm.getDormCode(),
                                room.getFloor(),
                                room.getRoomNumber(),
                                room.getRoomStatus(),
                                room.getCleanedAt(),
                                room.getCheckInAt(),
                                room.getCheckOutAt());
        }

        @Override
        @Transactional
        public int updateRoomStatusBulk(BulkRoomStatusUpdateDto dto, Instant now) {
                Dorm dorm = dormRepository.findByDormCode(dto.dormCode())
                                .orElseThrow(() -> new IllegalArgumentException("생활관 없음"));

                RoomStatus status = RoomStatus.valueOf(dto.newRoomStatus());

                // 일괄 업데이트
                return roomRepository.bulkStatusUpdate(
                                dorm,
                                dto.roomNumbers(),
                                status,
                                now);
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
