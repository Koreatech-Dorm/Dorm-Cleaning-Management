package com.dormclean.dorm_cleaning_management.controller.pages;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dormclean.dorm_cleaning_management.entity.Dorm;
import com.dormclean.dorm_cleaning_management.entity.Room;
import com.dormclean.dorm_cleaning_management.repository.DormRepository;
import com.dormclean.dorm_cleaning_management.repository.RoomRepository;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class RoomsPageController {
    private final DormRepository dormRepository;
    private final RoomRepository roomRepository;

    @GetMapping("/rooms")
    public String roomManager(Model model, @RequestParam(required = false) String dormCode) {
        List<Dorm> dormList = dormRepository.findAll();
        model.addAttribute("dorms", dormList);

        List<Integer> floors;

        if (dormCode != null && !dormCode.isEmpty()) {
            // 선택된 dorm 조회
            Dorm dorm = dormRepository.findByDormCode(dormCode).orElse(null);
            System.out.println(dorm);
            if (dorm != null) {
                floors = roomRepository.findByDorm(dorm).stream()
                        .map(Room::getFloor) // 층만 추출
                        .distinct() // 중복 제거
                        .sorted() // 오름차순 정렬
                        .toList();
            } else {
                floors = List.of(); // 빈 리스트
            }
        } else {
            // dorm 미선택 시 전체 층
            floors = roomRepository.findAll().stream()
                    .map(Room::getFloor)
                    .distinct()
                    .sorted()
                    .toList();
        }

        model.addAttribute("floors", floors);
        return "rooms";
    }
}
