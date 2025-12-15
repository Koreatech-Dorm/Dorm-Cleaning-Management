package com.dormclean.dorm_cleaning_management.controller;

import com.dormclean.dorm_cleaning_management.dto.room.*;
import com.dormclean.dorm_cleaning_management.entity.Room;
import com.dormclean.dorm_cleaning_management.service.RoomService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/api")
@Tag(name = "호실(방) 관리 API", description = "호실(방) 관련 API")
public class RoomController {
    private final RoomService roomService;

    // 모든 호실 정보 반환
    @GetMapping("/rooms/all")
    public ResponseEntity<List<RoomListResponseDto>> getAllRooms() {
        return ResponseEntity.ok(roomService.getRooms());
    }

    // 특정 생활관의 호실 정보 반환
    @GetMapping("/rooms/info/byDorm")
    public ResponseEntity<List<RoomListResponseDto>> getRoomsByDorm(@RequestParam("dormCode") String dormCode) {

        return ResponseEntity.ok(roomService.getRooms(dormCode));
    }

    @GetMapping("/rooms/info/byFloor")
    public ResponseEntity<List<RoomListResponseDto>> getRoomsByDormAndFloor(
            @RequestParam("dormCode") String dormCode,
            @RequestParam("floor") Integer floor) {

        return ResponseEntity.ok(roomService.getRooms(dormCode, floor));
    }

    // 호실 생성
    @PostMapping("/rooms/create")
    public ResponseEntity<Long> createRoom(@Valid @RequestBody CreateRoomRequestDto dto) {
        Room room = roomService.createRoom(dto);

        return ResponseEntity.ok(room.getId());
    }

    // 호실 상태 변경
    @PatchMapping("/rooms/{roomNumber}/status")
    public ResponseEntity<RoomListResponseDto> updateRoomStatus(
            @PathVariable("roomNumber") String roomNumber,
            @Valid @RequestBody RoomStatusUpdateDto dto) {
        RoomListResponseDto updatedRoom = roomService.updateRoomStatus(roomNumber, dto);

        return ResponseEntity.ok(updatedRoom);
    }

    @PatchMapping("/rooms/bulk-status")
    public ResponseEntity<BulkUpdateResponseDto> updateRoomsBulk(
            @RequestBody BulkRoomStatusUpdateDto dto) {
        Instant now = Instant.now();
        int updatedCount = roomService.updateRoomStatusBulk(dto, now);

        return ResponseEntity.ok(
                new BulkUpdateResponseDto(updatedCount, now));
    }

    // 생활관 층 목록 반환
    @GetMapping("/floors")
    public ResponseEntity<List<Integer>> getFloors(@RequestParam("dormCode") String dormCode) {
        List<Integer> floors = roomService.getFloors(dormCode);

        return ResponseEntity.ok(floors);
    }

    // 호실 삭제
    @DeleteMapping("/rooms/delete")
    public ResponseEntity<Void> deleteRoom(
            @RequestParam("dormCode") String dormCode,
            @RequestParam("roomNumber") String roomNumber) {
        roomService.deleteRoom(dormCode, roomNumber);

        return ResponseEntity.ok().build();
    }
}
