package com.dormclean.dorm_cleaning_management.service;

import com.dormclean.dorm_cleaning_management.entity.Dorm;
import com.dormclean.dorm_cleaning_management.entity.Room;
import com.dormclean.dorm_cleaning_management.entity.enums.RoomStatus;
import com.dormclean.dorm_cleaning_management.repository.RoomRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CheckServiceImpl implements CheckService{
    private final RoomRepository roomRepository;

    @Override
    public void checkOut(Dorm dorm, String roomNumber) {
        Room room = roomRepository.findByDormAndRoomNumber(dorm, roomNumber).orElseThrow(() -> new IllegalArgumentException("해당 호실이 존재하지 않습니다."));

        room.updateStatus(RoomStatus.VACANT_DIRTY);
    }

    @Override
    public void cleanCheck(Dorm dorm, String roomNumber){
        Room room = roomRepository.findByDormAndRoomNumber(dorm, roomNumber).orElseThrow(() -> new IllegalArgumentException("해당 호실이 존재하지 않습니다."));

        room.updateStatus(RoomStatus.VACANT_CLEAN);
        room.updateCleanedAt(java.time.Instant.now());
    }
}
