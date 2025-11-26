package com.dormclean.dorm_cleaning_management.controller;

import com.dormclean.dorm_cleaning_management.dto.CreateDormRequestDto;
import com.dormclean.dorm_cleaning_management.dto.CreateRoomRequestDto;
import com.dormclean.dorm_cleaning_management.entity.Dorm;
import com.dormclean.dorm_cleaning_management.entity.Room;
import com.dormclean.dorm_cleaning_management.service.DormService;
import com.dormclean.dorm_cleaning_management.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class DormController {

    private final DormService dormService;
    private final RoomService roomService;

    // 기숙사 생성
    @PostMapping("/dormitories")
    public ResponseEntity<Long> createdorm(@RequestBody CreateDormRequestDto dto) {
        Dorm dorm = dormService.createDorm(
                dto.dormCode(),
                dto.dormName());
        return ResponseEntity.ok(dorm.getId());
    }

    // 방 생성
    @PostMapping("/rooms")
    public ResponseEntity<Long> createRoom(@RequestBody CreateRoomRequestDto dto) {
        Room room = roomService.createRoom(
                dto.dormCode(),
                dto.floor(),
                dto.roomNumber());
        return ResponseEntity.ok(room.getId());
    }
}
