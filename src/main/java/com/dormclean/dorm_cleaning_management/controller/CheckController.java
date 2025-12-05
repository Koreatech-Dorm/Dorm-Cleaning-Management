package com.dormclean.dorm_cleaning_management.controller;

import com.dormclean.dorm_cleaning_management.dto.CheckRequestDto;
import com.dormclean.dorm_cleaning_management.entity.Dorm;
import com.dormclean.dorm_cleaning_management.repository.DormRepository;
import com.dormclean.dorm_cleaning_management.service.CheckService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/check")
@RequiredArgsConstructor
@Tag(name = "방 상태 관리 API", description = "퇴실 확인 및 청소 완료 처리 API")
public class CheckController {
    private final CheckService checkService;
    private final DormRepository dormRepository;

    @PostMapping("/in")
    public ResponseEntity<Map<String, String>> checkIn(@RequestBody CheckRequestDto dto) {
        Dorm dorm = dormRepository.findByDormCode(dto.dormCode())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 기숙사입니다."));
        checkService.checkIn(dorm, dto.roomNumber());

        return ResponseEntity.ok(Map.of("message", "입실 처리가 완료되었습니다."));
    }

    @PostMapping("/out")
    public ResponseEntity<Map<String, String>> checkOut(@RequestBody CheckRequestDto dto) {
        Dorm dorm = dormRepository.findByDormCode(dto.dormCode())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 기숙사입니다."));
        checkService.checkOut(dorm, dto.roomNumber());

        return ResponseEntity.ok(Map.of("message", "퇴실 처리가 완료되었습니다."));
    }

    @PostMapping("/clean")
    public ResponseEntity<Map<String, String>> cleanCheck(@RequestBody CheckRequestDto dto) {
        Dorm dorm = dormRepository.findByDormCode(dto.dormCode())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 기숙사입니다."));
        checkService.cleanCheck(dorm, dto.roomNumber());

        return ResponseEntity.ok(Map.of("message", "청소 처리가 완료되었습니다."));
    }
}
