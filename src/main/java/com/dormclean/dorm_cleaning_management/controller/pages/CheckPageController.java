package com.dormclean.dorm_cleaning_management.controller.pages;

import com.dormclean.dorm_cleaning_management.dto.QrResponseDto;
import com.dormclean.dorm_cleaning_management.entity.Dorm;
import com.dormclean.dorm_cleaning_management.entity.Room;
import com.dormclean.dorm_cleaning_management.repository.CleaningCodeRepository;
import com.dormclean.dorm_cleaning_management.repository.DormRepository;
import com.dormclean.dorm_cleaning_management.repository.RoomRepository;
import com.dormclean.dorm_cleaning_management.service.QrCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class CheckPageController {

    private final QrCodeService qrCodeService;
    private final DormRepository dormRepository;
    private final RoomRepository roomRepository;

    @GetMapping("/check")
    public String check(@RequestParam("token") String token, Model model) {
        // 서비스에서 토큰으로 기숙사/호실 정보 가져오기
        QrResponseDto data = qrCodeService.getQrData(token);

        Dorm dorm = dormRepository.findByDormCode(data.dormCode())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 기숙사입니다."));
        Room room = roomRepository.findByDormAndRoomNumber(dorm, data.roomNumber())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 호실입니다."));

        // HTML(뷰)에 데이터 전달
        model.addAttribute("dormName", dorm.getDormName());
        model.addAttribute("dormCode", dorm.getDormCode());
        model.addAttribute("roomNumber", room.getRoomNumber());
        model.addAttribute("status", room.getRoomStatus().name());

        // check.html 파일을 보여줌
        return "check";
    }
}