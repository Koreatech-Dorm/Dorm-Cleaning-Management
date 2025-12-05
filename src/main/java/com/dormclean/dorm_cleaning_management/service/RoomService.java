package com.dormclean.dorm_cleaning_management.service;

import java.util.List;

import com.dormclean.dorm_cleaning_management.dto.CreateRoomRequestDto;
import com.dormclean.dorm_cleaning_management.entity.Dorm;
import com.dormclean.dorm_cleaning_management.entity.Room;

public interface RoomService {
    public Room createRoom(CreateRoomRequestDto dto);

    public List<Room> getRoomsByDorm(Dorm dorm);

    public List<Room> getRoomsByDormAndFloor(Dorm dorm, Integer floor);

    public void updateRoomStatus(String dormCode, String roomNumber, String newRoomStatus);

    public void deleteRoom(String dormCode, String roomNumber);

}