package com.dormclean.dorm_cleaning_management.service.check;

import com.dormclean.dorm_cleaning_management.dto.check.CheckRequestDto;
import com.dormclean.dorm_cleaning_management.dto.cleaningCode.CleaningCodeDto;
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
        Room room = findRoomStatus(dto);

        if (room.getRoomStatus() != RoomStatus.READY) {
            throw new IllegalStateException("준비된 방만 입실이 가능합니다.");
        }
        room.checkIn();
    }

    @Override
    public void checkOut(CheckRequestDto dto) {
        Room room = findRoomStatus(dto);

        if (room.getRoomStatus() != RoomStatus.OCCUPIED) {
            throw new IllegalStateException("재실 중인 방만 퇴실이 가능합니다.");
        }
        room.checkOut();
    }

    @Override
    public void cleanCheck(CheckRequestDto dto) {
        Room room = findRoomStatus(dto);

        if (room.getRoomStatus() != RoomStatus.VACANT) {
            throw new IllegalStateException("공실인 방만 청소가 가능합니다.");
        }
        room.clean();
    }

    private Room findRoomStatus(CheckRequestDto dto){
        Room room = roomRepository.findRoomByDormCodeAndRoomNumber(dto.dormCode(), dto.roomNumber());

        return room;
    }

    @Override
    public void useCleaningCode(CleaningCodeDto dto) {
        CleaningCode checkCode = cleaningCodeRepository.findByCleaningCode(dto.cleaningCode())
                .orElseThrow(() -> new IllegalArgumentException("코드가 유효하지 않습니다."));

        session.setAttribute("cleaningCode", checkCode.getCleaningCode());
        session.setMaxInactiveInterval(21600); // 6시간
    }
}
