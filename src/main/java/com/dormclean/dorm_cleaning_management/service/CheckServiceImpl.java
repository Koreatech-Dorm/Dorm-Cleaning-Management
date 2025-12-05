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
public class CheckServiceImpl implements CheckService {
    private final RoomRepository roomRepository;

    @Override
    public void checkIn(Dorm dorm, String roomNumber) {
        Room room = roomRepository.findByDormAndRoomNumber(dorm, roomNumber)
                .orElseThrow(() -> new IllegalArgumentException("해당 호실이 존재하지 않습니다."));
        if(room.getRoomStatus() == RoomStatus.READY){
            room.updateStatus(RoomStatus.OCCUPIED);
            room.updateCheckInAt(java.time.Instant.now());
        }
    }

    @Override
    public void checkOut(Dorm dorm, String roomNumber) {
        Room room = roomRepository.findByDormAndRoomNumber(dorm, roomNumber)
                .orElseThrow(() -> new IllegalArgumentException("해당 호실이 존재하지 않습니다."));

        if(room.getRoomStatus() == RoomStatus.OCCUPIED){
            room.updateStatus(RoomStatus.VACANT_DIRTY);
            room.updateCheckOutAt(java.time.Instant.now());
        }
    }

    @Override
    public void cleanCheck(Dorm dorm, String roomNumber) {
        Room room = roomRepository.findByDormAndRoomNumber(dorm, roomNumber)
                .orElseThrow(() -> new IllegalArgumentException("해당 호실이 존재하지 않습니다."));

        if(room.getRoomStatus() == RoomStatus.VACANT_DIRTY){
            room.updateStatus(RoomStatus.READY);
            room.updateCleanedAt(java.time.Instant.now());
        }else{
            throw new IllegalStateException("퇴실하지 않은 상태에서는 청소할 수 없습니다.");
        }
    }
}
