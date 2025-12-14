package com.dormclean.dorm_cleaning_management.service;

import com.dormclean.dorm_cleaning_management.dto.room.CreateRoomRequestDto;
import com.dormclean.dorm_cleaning_management.dto.room.RoomListResponseDto;
import com.dormclean.dorm_cleaning_management.dto.room.RoomStatusUpdateDto;
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

        // 특정 Dorm + Floor의 방 목록 조회
        @Override
        public List<RoomListResponseDto> getRooms(String dormCode, Integer floor) {
                Dorm dorm = dormRepository.findByDormCode(dormCode)
                                .orElseThrow(() -> new RuntimeException("Dorm not found"));
                List<Room> rooms = roomRepository.findByDormAndFloor(dorm, floor);

                return rooms.stream()
                                .map(this::toRoomListDto)
                                .toList();
        }

        // 특정 Dorm의 모든 방 조회 (층 목록 등)
        @Override
        public List<RoomListResponseDto> getRooms(String dormCode) {
                Dorm dorm = dormRepository.findByDormCode(dormCode)
                                .orElseThrow(() -> new IllegalArgumentException("Dorm not found"));

                List<Room> rooms = roomRepository.findByDorm(dorm);

                return rooms.stream()
                                .map(this::toRoomListDto)
                                .toList();
        }

        private RoomListResponseDto toRoomListDto(Room r) {
                return new RoomListResponseDto(
                                r.getDorm().getDormCode(),
                                r.getFloor(),
                                r.getRoomNumber(),
                                r.getStatus(),
                                r.getCleanedAt(),
                                r.getCheckInAt(),
                                r.getCheckOutAt());
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
        public void updateRoomStatus(String roomNumber, RoomStatusUpdateDto dto) {
                Dorm dorm = dormRepository.findByDormCode(dto.dormCode())
                                .orElseThrow(() -> new IllegalArgumentException("해당 생활관을 찾을 수 없습니다."));
                Room room = roomRepository.findByDormAndRoomNumber(dorm, roomNumber)
                                .orElseThrow(() -> new IllegalArgumentException("해당 호실을 찾을 수 없습니다."));

                if (dto.newRoomStatus().equals("OCCUPIED")) {
                        room.updateStatus(RoomStatus.OCCUPIED);
                        room.updateCheckInAt(java.time.Instant.now());
                } else if (dto.newRoomStatus().equals("READY")) {
                        room.updateStatus(RoomStatus.READY);
                        room.updateCleanedAt(java.time.Instant.now());
                } else {
                        room.updateStatus(RoomStatus.VACANT);
                        room.updateCheckOutAt(java.time.Instant.now());
                }
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
