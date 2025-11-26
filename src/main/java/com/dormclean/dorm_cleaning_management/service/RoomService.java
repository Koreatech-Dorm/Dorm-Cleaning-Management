package com.dormclean.dorm_cleaning_management.service;

import com.dormclean.dorm_cleaning_management.entity.Dorm;
import com.dormclean.dorm_cleaning_management.entity.Room;
import com.dormclean.dorm_cleaning_management.repository.DormRepository;
import com.dormclean.dorm_cleaning_management.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final DormRepository dormRepository;

    public Room createRoom(String dormCode, Integer floor, String roomNumber) {
        Dorm dorm = dormRepository.findByDormCode(dormCode);
        if (dorm == null) {
            throw new IllegalArgumentException("해당 dormCode의 기숙사가 없습니다.");
        }

        Room room = Room.builder()
                .dorm(dorm)
                .floor(floor)
                .roomNumber(roomNumber)
                .status(Room.RoomStatus.OCCUPIED)
                .build();

        return roomRepository.save(room);
    }
}
