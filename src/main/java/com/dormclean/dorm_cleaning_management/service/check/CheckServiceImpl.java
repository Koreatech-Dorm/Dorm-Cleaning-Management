package com.dormclean.dorm_cleaning_management.service.check;

import com.dormclean.dorm_cleaning_management.dto.check.CheckRequestDto;
import com.dormclean.dorm_cleaning_management.dto.cleaningCode.CleaningCodeDto;
import com.dormclean.dorm_cleaning_management.entity.CleaningCode;
import com.dormclean.dorm_cleaning_management.entity.Room;
import com.dormclean.dorm_cleaning_management.entity.enums.RoomStatus;
import com.dormclean.dorm_cleaning_management.exception.cleaningCode.InvalidCleaningCodeException;
import com.dormclean.dorm_cleaning_management.exception.room.RoomCheckInNotAllowedException;
import com.dormclean.dorm_cleaning_management.exception.room.RoomCheckOutNotAllowedException;
import com.dormclean.dorm_cleaning_management.exception.room.RoomCleanNotAllowedException;
import com.dormclean.dorm_cleaning_management.repository.CleaningCodeRepository;
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
    private final CleaningCodeRepository cleaningCodeRepository;

    @Override
    @Transactional
    public void checkIn(CheckRequestDto dto) {
        int updatedCount = roomRepository.updateStatusToCheckIn(dto.dormCode(), dto.roomNumber());

        if (updatedCount == 0) {
            throw new RoomCheckInNotAllowedException();
        }
    }

    @Override
    @Transactional
    public void checkOut(CheckRequestDto dto) {
        int updatedCount = roomRepository.updateStatusToCheckOut(dto.dormCode(), dto.roomNumber());

        if (updatedCount == 0) {
            throw new RoomCheckOutNotAllowedException();
        }
    }

    @Override
    @Transactional
    public void cleanCheck(CheckRequestDto dto) {
        int updatedCount = roomRepository.updateStatusToCleaned(dto.dormCode(), dto.roomNumber());

        if (updatedCount == 0) {
            throw new RoomCleanNotAllowedException();
        }
    }

    @Override
    public void useCleaningCode(CleaningCodeDto dto) {
        CleaningCode checkCode = cleaningCodeRepository.findByCleaningCode(dto.cleaningCode())
                .orElseThrow(InvalidCleaningCodeException::new);

        session.setAttribute("cleaningCode", checkCode.getCleaningCode());
        session.setMaxInactiveInterval(21600); // 6시간
    }
}
