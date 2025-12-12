package com.dormclean.dorm_cleaning_management.controller;

import com.dormclean.dorm_cleaning_management.dto.dorm.CreateDormRequestDto;
import com.dormclean.dorm_cleaning_management.dto.dorm.DormDeleteRequestDto;
import com.dormclean.dorm_cleaning_management.dto.dorm.DormListResponseDto;
import com.dormclean.dorm_cleaning_management.entity.Dorm;
import com.dormclean.dorm_cleaning_management.service.DormService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/api")
@Tag(name = "기숙사 건물 관리 API", description = "기숙사 관련 API")
public class DormController {

    private final DormService dormService;

    // 기숙사 리스트 반환
    @GetMapping("/dorms")
    public ResponseEntity<List<DormListResponseDto>> getAllDorms() {
        List<DormListResponseDto> dtoList = dormService.findAllDorms();
        return ResponseEntity.ok(dtoList);
    }

    // 기숙사 생성
    @PostMapping("/dorms/create")
    public ResponseEntity<Long> createdorm(@Valid @RequestBody CreateDormRequestDto dto) {
        Dorm dorm = dormService.createDorm(dto);

        return ResponseEntity.ok(dorm.getId());
    }

    // 기숙사 삭제
    @DeleteMapping("/dorms/{dormCode}")
    public ResponseEntity<Void> deleteDorm(@Valid @PathVariable("dormCode") DormDeleteRequestDto dto) {
        dormService.deleteDorm(dto);

        return ResponseEntity.ok().build();
    }

}
