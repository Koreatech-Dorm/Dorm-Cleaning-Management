package com.dormclean.dorm_cleaning_management.service.room;

import java.time.Instant;
import java.util.List;

import com.dormclean.dorm_cleaning_management.dto.room.*;
import com.dormclean.dorm_cleaning_management.entity.Room;

public interface RoomService {
    Room createRoom(CreateRoomRequestDto dto);

    List<RoomListResponseDto> getRooms();

    List<RoomListResponseDto> getRooms(String dormCode);

    List<RoomListResponseDto> getRooms(String dormCode, Integer floor);

    List<Integer> getFloors(String dormCode);

    RoomListResponseDto updateRoomStatus(String roomNumber, RoomStatusUpdateDto dto);

    int updateRoomStatusBulk(BulkRoomStatusUpdateDto dto, Instant now);

    void deleteRoom(String dormCode, String roomNumber);
}