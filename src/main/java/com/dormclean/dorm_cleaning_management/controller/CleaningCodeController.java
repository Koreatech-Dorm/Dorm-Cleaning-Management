package com.dormclean.dorm_cleaning_management.controller;

import com.dormclean.dorm_cleaning_management.dto.CleaningCodeDto;
import com.dormclean.dorm_cleaning_management.dto.GetCleaningCodeResponseDto;
import com.dormclean.dorm_cleaning_management.dto.RegistrationCleaningCodeRequestDto;
import com.dormclean.dorm_cleaning_management.service.CleaningCodeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/api/cleaning-code")
@Tag(name = "청소 인증 코드 API", description = "청소 인증 코드 등록 및 검증 관련 API")
public class CleaningCodeController {
    private final CleaningCodeService cleaningCodeService;

    @PostMapping("/registration")
    public void registration(@RequestBody RegistrationCleaningCodeRequestDto dto) {
        cleaningCodeService.registration(dto);
    }

    @GetMapping("/get-code")
    public ResponseEntity<GetCleaningCodeResponseDto> getAllCleaningCode() {
        GetCleaningCodeResponseDto dto = cleaningCodeService.getCleaningCode();

        if (dto == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(dto);
    }
}
