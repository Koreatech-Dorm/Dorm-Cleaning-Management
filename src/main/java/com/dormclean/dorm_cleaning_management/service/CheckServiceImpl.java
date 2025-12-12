package com.dormclean.dorm_cleaning_management.service;

import com.dormclean.dorm_cleaning_management.dto.CheckRequestDto;
import com.dormclean.dorm_cleaning_management.dto.CleaningCodeDto;
import com.dormclean.dorm_cleaning_management.entity.CleaningCode;
import com.dormclean.dorm_cleaning_management.entity.Dorm;
import com.dormclean.dorm_cleaning_management.entity.Room;
import com.dormclean.dorm_cleaning_management.entity.enums.RoomStatus;
import com.dormclean.dorm_cleaning_management.repository.CleaningCodeRepository;
import com.dormclean.dorm_cleaning_management.repository.DormRepository;
import com.dormclean.dorm_cleaning_management.repository.RoomRepository;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CheckServiceImpl implements CheckService {
    private final HttpSession session;
    private final RoomRepository roomRepository;
    private final DormRepository dormRepository;
    private final CleaningCodeRepository cleaningCodeRepository;

    @Override
    public void checkIn(CheckRequestDto dto) {
        Dorm dorm = dormRepository.findByDormCode(dto.dormCode())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 기숙사입니다."));
        Room room = roomRepository.findByDormAndRoomNumber(dorm, dto.roomNumber())
                .orElseThrow(() -> new IllegalArgumentException("해당 호실이 존재하지 않습니다."));
        if (room.getRoomStatus() == RoomStatus.READY) {
            room.updateStatus(RoomStatus.OCCUPIED); // 재실
            room.updateCheckInAt(java.time.Instant.now()); // 체크인 시간 업데이트
            room.updateCheckOutAt(null); // 퇴실 시간 초기화
            room.updateCleanedAt(null); // 청소 시간 초기화
        }
    }

    @Override
    public void checkOut(CheckRequestDto dto) {
        Dorm dorm = dormRepository.findByDormCode(dto.dormCode())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 기숙사입니다."));
        Room room = roomRepository.findByDormAndRoomNumber(dorm, dto.roomNumber())
                .orElseThrow(() -> new IllegalArgumentException("해당 호실이 존재하지 않습니다."));

        if (room.getRoomStatus() == RoomStatus.OCCUPIED) {
            room.updateStatus(RoomStatus.VACANT);
            room.updateCheckOutAt(java.time.Instant.now());
        }
    }

    @Override
    public void cleanCheck(CheckRequestDto dto) {
        Dorm dorm = dormRepository.findByDormCode(dto.dormCode())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 기숙사입니다."));
        Room room = roomRepository.findByDormAndRoomNumber(dorm, dto.roomNumber())
                .orElseThrow(() -> new IllegalArgumentException("해당 호실이 존재하지 않습니다."));

        if (room.getRoomStatus() == RoomStatus.VACANT) {
            room.updateStatus(RoomStatus.READY);
            room.updateCleanedAt(java.time.Instant.now());
        } else {
            throw new IllegalStateException("퇴실하지 않은 상태에서는 청소할 수 없습니다.");
        }
    }

    @Override
    public void useCleaningCode(CleaningCodeDto dto) {
        CleaningCode checkCode = cleaningCodeRepository.findByCleaningCode(dto.cleaningCode())
                .orElseThrow(() -> new IllegalArgumentException("코드가 유효하지 않습니다."));

        session.setAttribute("cleaningCode", checkCode.getCleaningCode());
        session.setMaxInactiveInterval(21600); // 6시간
    }
}
