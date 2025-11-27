package com.dormclean.dorm_cleaning_management.service;

import com.dormclean.dorm_cleaning_management.entity.Dorm;
import com.dormclean.dorm_cleaning_management.entity.Room;
import com.dormclean.dorm_cleaning_management.entity.enums.RoomStatus;
import com.dormclean.dorm_cleaning_management.repository.RoomRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class CheckServiceImpl implements CheckService{
    private final RoomRepository roomRepository;

    @Override
    public void checkOut(Dorm dorm, String roomNumber) {
        Room room = roomRepository.findByDormAndRoomNumber(dorm, roomNumber).orElse(null);
        if (room != null) {
            room.updateStatus(RoomStatus.VACANT_DIRTY);
            roomRepository.save(room);
        }
    }

    @Override
    public void cleanCheck(Dorm dorm, String roomNumber){
        Room room = roomRepository.findByDormAndRoomNumber(dorm, roomNumber).orElse(null);
        if (room != null) {
            room.updateStatus(RoomStatus.VACANT_CLEAN);
            roomRepository.save(room);
        }
    }
}
