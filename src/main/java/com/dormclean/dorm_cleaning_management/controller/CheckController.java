package com.dormclean.dorm_cleaning_management.controller;

import com.dormclean.dorm_cleaning_management.dto.check.CheckRequestDto;
import com.dormclean.dorm_cleaning_management.dto.cleaningCode.CleaningCodeDto;
import com.dormclean.dorm_cleaning_management.service.check.CheckService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "방 상태 관리 API", description = "퇴실 확인 및 청소 완료 처리 API")
public class CheckController {
    private final CheckService checkService;

    @PatchMapping("/check/in")
    public ResponseEntity<Map<String, String>> checkIn(@RequestBody CheckRequestDto dto) {
        checkService.checkIn(dto);

        return ResponseEntity.ok(Map.of("message", "입실 처리가 완료되었습니다."));
    }

    @PatchMapping("/check/out")
    public ResponseEntity<Map<String, String>> checkOut(@RequestBody CheckRequestDto dto) {
        checkService.checkOut(dto);

        return ResponseEntity.ok(Map.of("message", "퇴실 처리가 완료되었습니다."));
    }

    @PatchMapping("/check/clean")
    public ResponseEntity<Map<String, String>> cleanCheck(@RequestBody CheckRequestDto dto) {
        checkService.cleanCheck(dto);

        return ResponseEntity.ok(Map.of("message", "청소 처리가 완료되었습니다."));
    }

    @PostMapping("/check/use-code")
    public ResponseEntity<String> useCode(@Valid @RequestBody CleaningCodeDto dto) {
        checkService.useCleaningCode(dto);

        return ResponseEntity.ok("코드 인증되었습니다.");
    }
}
