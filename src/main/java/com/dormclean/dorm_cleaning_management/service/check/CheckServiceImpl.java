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
    public void checkIn(CheckRequestDto dto) {
        Room room = findRoomStatus(dto);

        if (room.getRoomStatus() != RoomStatus.READY) {
            throw new RoomCheckInNotAllowedException();
        }
        room.checkIn();
    }

    @Override
    public void checkOut(CheckRequestDto dto) {
        Room room = findRoomStatus(dto);

        if (room.getRoomStatus() != RoomStatus.OCCUPIED) {
            throw new RoomCheckOutNotAllowedException();
        }
        room.checkOut();
    }

    @Override
    public void cleanCheck(CheckRequestDto dto) {
        Room room = findRoomStatus(dto);

        if (room.getRoomStatus() != RoomStatus.VACANT) {
            throw new RoomCleanNotAllowedException();
        }
        room.clean();
    }

    private Room findRoomStatus(CheckRequestDto dto) {
        Room room = roomRepository.findRoomByDormCodeAndRoomNumber(dto.dormCode(), dto.roomNumber());

        return room;
    }

    @Override
    public void useCleaningCode(CleaningCodeDto dto) {
        CleaningCode checkCode = cleaningCodeRepository.findByCleaningCode(dto.cleaningCode())
                .orElseThrow(InvalidCleaningCodeException::new);

        session.setAttribute("cleaningCode", checkCode.getCleaningCode());
        session.setMaxInactiveInterval(21600); // 6시간
    }
}
