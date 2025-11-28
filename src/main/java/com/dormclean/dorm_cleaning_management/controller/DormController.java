package com.dormclean.dorm_cleaning_management.controller;

import com.dormclean.dorm_cleaning_management.dto.CreateDormRequestDto;
import com.dormclean.dorm_cleaning_management.entity.Dorm;
import com.dormclean.dorm_cleaning_management.service.DormService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "기숙사 건물 관리 API", description = "기숙사 관련 API")
public class DormController {

    private final DormService dormService;

    // 기숙사 생성
    @PostMapping("/dormitories")
    public ResponseEntity<Long> createdorm(@RequestBody CreateDormRequestDto dto) {
        Dorm dorm = dormService.createDorm(
                dto.dormCode(),
                dto.dormName());
        return ResponseEntity.ok(dorm.getId());
    }
}
