package com.dormclean.dorm_cleaning_management.service.room;

import com.dormclean.dorm_cleaning_management.dto.room.*;
import com.dormclean.dorm_cleaning_management.entity.Dorm;
import com.dormclean.dorm_cleaning_management.entity.Room;
import com.dormclean.dorm_cleaning_management.entity.enums.RoomStatus;
import com.dormclean.dorm_cleaning_management.exception.dorm.DormNotFoundException;
import com.dormclean.dorm_cleaning_management.exception.room.FloorLoadFailedException;
import com.dormclean.dorm_cleaning_management.exception.room.InvalidRoomStatusException;
import com.dormclean.dorm_cleaning_management.exception.room.RoomAlreadyExistsException;
import com.dormclean.dorm_cleaning_management.exception.room.RoomLoadFailedException;
import com.dormclean.dorm_cleaning_management.exception.room.RoomNotFoundException;
import com.dormclean.dorm_cleaning_management.repository.DormRepository;
import com.dormclean.dorm_cleaning_management.repository.RoomRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
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
                                .orElseThrow(DormNotFoundException::new);

                if (roomRepository.existsByDormAndRoomNumber(dorm, dto.roomNumber())) {
                        throw new RoomAlreadyExistsException();
                }

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
                try {
                        return roomRepository.findAllRoomsDto();
                } catch (Exception e) {
                        throw new RoomLoadFailedException();
                }
        }

        // 특정 Dorm의 모든 방 조회 (층 목록 등)
        @Override
        @Transactional(readOnly = true)
        public List<RoomListResponseDto> getRooms(String dormCode) {
                try {
                        return roomRepository.findRoomByDormCode(dormCode);
                } catch (Exception e) {
                        throw new RoomLoadFailedException();
                }
        }

        // 특정 Dorm + Floor의 방 목록 조회
        @Override
        @Transactional(readOnly = true)
        public List<RoomListResponseDto> getRooms(String dormCode, Integer floor) {
                try {
                        return roomRepository.findRoomByDormCodeAndFloor(dormCode, floor);
                } catch (Exception e) {
                        throw new RoomLoadFailedException();
                }
        }

        @Override
        public List<Integer> getFloors(String dormCode) {
                Dorm dorm = dormRepository.findByDormCode(dormCode)
                                .orElseThrow(DormNotFoundException::new);
                try {
                        return roomRepository.findByDorm(dorm).stream()
                                        .map(Room::getFloor)
                                        .distinct()
                                        .sorted()
                                        .toList();
                } catch (Exception e) {
                        throw new FloorLoadFailedException();
                }
        }

        // 호실 상태 변경
        @Override
        @Transactional
        public RoomListResponseDto updateRoomStatus(
                        String roomNumber,
                        RoomStatusUpdateDto dto) {
                Room room = roomRepository.findByDormCodeAndRoomNumber(dto.dormCode(), roomNumber)
                                .orElseThrow(RoomNotFoundException::new);

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
                        default -> throw new InvalidRoomStatusException();
                }

                return new RoomListResponseDto(
                                dto.dormCode(),
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
                                .orElseThrow(DormNotFoundException::new);

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
                Room room = roomRepository.findByDormCodeAndRoomNumber(dormCode, roomNumber)
                                .orElseThrow(RoomNotFoundException::new);

                roomRepository.delete(room);
        }

        // 호실 삭제
        @Override
        @Transactional
        public int deleteRoomsByDormCode(String dormCode, List<String> roomNumbers) {
                if (roomNumbers == null || roomNumbers.isEmpty()) {
                        return 0;
                }
                return roomRepository.deleteByDormCodeAndRoomNumbers(dormCode, roomNumbers);
        }
}
