package com.dormclean.dorm_cleaning_management.controller;

import com.dormclean.dorm_cleaning_management.dto.CreateRoomRequestDto;
import com.dormclean.dorm_cleaning_management.dto.RoomDto;
import com.dormclean.dorm_cleaning_management.dto.RoomStatusUpdateDto;
import com.dormclean.dorm_cleaning_management.entity.Dorm;
import com.dormclean.dorm_cleaning_management.entity.Room;
import com.dormclean.dorm_cleaning_management.repository.DormRepository;
import com.dormclean.dorm_cleaning_management.service.RoomService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "호실(방) 관리 API", description = "호실(방) 관련 API")
public class RoomController {

        private final RoomService roomService;
        private final DormRepository dormRepository;

        // 특정 생활관의 호실 정보 반환
        @GetMapping(value = "/rooms/info", params = "!floor")
        public ResponseEntity<List<RoomDto>> getRoomsByDorm(@RequestParam String dormCode) {

                Dorm dorm = dormRepository.findByDormCode(dormCode)
                                .orElseThrow(() -> new RuntimeException("Dorm not found"));

                List<RoomDto> rooms = roomService.getRoomsByDorm(dorm)
                                .stream()
                                .map(r -> new RoomDto(r.getId(), r.getRoomNumber(), r.getRoomStatus(),
                                                r.getStatusLabel()))
                                .collect(Collectors.toList());

                return ResponseEntity.ok(rooms);
        }

        @GetMapping(value = "/rooms/info", params = "floor")
        public ResponseEntity<List<RoomDto>> getRoomsByDormAndFloor(
                        @RequestParam String dormCode,
                        @RequestParam Integer floor) {

                Dorm dorm = dormRepository.findByDormCode(dormCode)
                                .orElseThrow(() -> new RuntimeException("Dorm not found"));

                List<RoomDto> rooms = roomService.getRoomsByDormAndFloor(dorm, floor)
                                .stream()
                                .map(r -> new RoomDto(r.getId(), r.getRoomNumber(), r.getRoomStatus(),
                                                r.getStatusLabel()))
                                .collect(Collectors.toList());

                return ResponseEntity.ok(rooms);
        }

        // 호실 생성
        @PostMapping("/rooms/create")
        public ResponseEntity<Long> createRoom(@RequestBody CreateRoomRequestDto dto) {
                Room room = roomService.createRoom(dto);
                return ResponseEntity.ok(room.getId());
        }

        // 호실 상태 변경
        @PatchMapping("/rooms/{roomId}/status")
        public ResponseEntity<Void> updateRoomStatus(
                        @PathVariable Long roomId,
                        @RequestBody RoomStatusUpdateDto dto) {
                roomService.updateRoomStatus(roomId, dto.roomStatus());
                return ResponseEntity.ok().build();
        }

        // 생활관 층 목록 반환
        @GetMapping("/floors")
        public ResponseEntity<List<Integer>> getFloors(@RequestParam String dormCode) {
                Dorm dorm = dormRepository.findByDormCode(dormCode)
                                .orElseThrow(() -> new RuntimeException("Dorm not found"));

                List<Integer> floors = roomService.getRoomsByDorm(dorm)
                                .stream()
                                .map(Room::getFloor)
                                .distinct()
                                .sorted()
                                .collect(Collectors.toList());

                return ResponseEntity.ok(floors);
        }

        // 호실 삭제
        @DeleteMapping("/rooms/delete")
        public ResponseEntity<Void> deleteRoom(
                        @RequestParam String dormCode,
                        @RequestParam String roomNumber){
                roomService.deleteRoom(dormCode, roomNumber);

                return ResponseEntity.ok().build();
        }
}
