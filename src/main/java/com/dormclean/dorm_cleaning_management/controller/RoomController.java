package com.dormclean.dorm_cleaning_management.controller;

import com.dormclean.dorm_cleaning_management.dto.CreateRoomRequestDto;
import com.dormclean.dorm_cleaning_management.dto.RoomDto;
import com.dormclean.dorm_cleaning_management.dto.RoomStatusUpdateDto;
import com.dormclean.dorm_cleaning_management.entity.Dorm;
import com.dormclean.dorm_cleaning_management.entity.Room;
import com.dormclean.dorm_cleaning_management.repository.DormRepository;
import com.dormclean.dorm_cleaning_management.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class RoomController {

    private final RoomService roomService;
    private final DormRepository dormRepository;

    // 1️⃣ 방 생성
    @PostMapping("/rooms")
    public ResponseEntity<Long> createRoom(@RequestBody CreateRoomRequestDto dto) {
        Room room = roomService.createRoom(dto.dormCode(), dto.floor(), dto.roomNumber());
        return ResponseEntity.ok(room.getId());
    }

    // 2️⃣ 특정 Dorm + Floor의 Room 리스트 반환
    @GetMapping("/rooms")
    public ResponseEntity<List<RoomDto>> getRooms(
            @RequestParam Long dormId,
            @RequestParam Integer floor) {

        Dorm dorm = dormRepository.findById(dormId)
                .orElseThrow(() -> new RuntimeException("Dorm not found"));

        List<RoomDto> rooms = roomService.getRoomsByDormAndFloor(dorm, floor)
                .stream()
                .map(r -> new RoomDto(r.getId(), r.getRoomNumber(), r.getStatus(), r.getStatusLabel()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(rooms);
    }

    // 3️⃣ Room 상태 변경
    @PatchMapping("/rooms/{roomId}/status")
    public ResponseEntity<Void> updateRoomStatus(
            @PathVariable Long roomId,
            @RequestBody RoomStatusUpdateDto dto) {
        roomService.updateRoomStatus(roomId, dto.status());
        return ResponseEntity.ok().build();
    }

    // 4️⃣ 특정 Dorm의 층 목록 반환
    @GetMapping("/floors")
    public ResponseEntity<List<Integer>> getFloors(@RequestParam Long dormId) {
        Dorm dorm = dormRepository.findById(dormId)
                .orElseThrow(() -> new RuntimeException("Dorm not found"));

        List<Integer> floors = roomService.getRoomsByDorm(dorm)
                .stream()
                .map(Room::getFloor)
                .distinct()
                .sorted()
                .collect(Collectors.toList());

        return ResponseEntity.ok(floors);
    }
}
