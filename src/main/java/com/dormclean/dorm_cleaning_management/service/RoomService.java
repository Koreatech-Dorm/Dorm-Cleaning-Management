package com.dormclean.dorm_cleaning_management.service;

import java.util.List;

import com.dormclean.dorm_cleaning_management.dto.CreateRoomRequestDto;
import com.dormclean.dorm_cleaning_management.dto.RoomListResponseDto;
import com.dormclean.dorm_cleaning_management.dto.RoomStatusUpdateDto;
import com.dormclean.dorm_cleaning_management.entity.Room;

public interface RoomService {
    Room createRoom(CreateRoomRequestDto dto);

    List<RoomListResponseDto> getRooms(String dormCode);

    List<RoomListResponseDto> getRooms(String dormCode, Integer floor);

    List<Integer> getFloors(String dormCode);

    void updateRoomStatus(String roomNumber, RoomStatusUpdateDto dto);

    void deleteRoom(String dormCode, String roomNumber);

}