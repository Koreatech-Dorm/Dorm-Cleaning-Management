package com.dormclean.dorm_cleaning_management.controller;

import com.dormclean.dorm_cleaning_management.dto.CreateDormRequestDto;
import com.dormclean.dorm_cleaning_management.dto.DormUpdateRequestDto;
import com.dormclean.dorm_cleaning_management.entity.Dorm;
import com.dormclean.dorm_cleaning_management.service.DormService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "기숙사 건물 관리 API", description = "기숙사 관련 API")
public class DormController {

    private final DormService dormService;

    // 기숙사 리스트 반환
    @GetMapping("/dorms")
    public ResponseEntity<List<Dorm>> getAllDorms() {
        List<Dorm> dorms = dormService.findAllDorms();
        return ResponseEntity.ok(dorms);
    }

    // 기숙사 생성
    @PostMapping("/dorms/create")
    public ResponseEntity<Long> createdorm(@RequestBody CreateDormRequestDto dto) {
        Dorm dorm = dormService.createDorm(
                dto.dormCode(),
                dto.dormName());
        return ResponseEntity.ok(dorm.getId());
    }

    // 기숙사 삭제
    @DeleteMapping("/dorms/{dormCode}")
    public ResponseEntity<Void> deleteDorm(@PathVariable String dormCode) {
        dormService.deleteDorm(dormCode);
        return ResponseEntity.ok().build();
    }

    // 기숙사 정보 업데이트
    @PostMapping("/dorms/update")
    public ResponseEntity<?> updateDorm(@RequestBody DormUpdateRequestDto dto) {
        dormService.updateDorm(dto.dormCode(), dto.dormName());
        return ResponseEntity.ok("updated");
    }
}
