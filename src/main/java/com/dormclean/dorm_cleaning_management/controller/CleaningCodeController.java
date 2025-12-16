package com.dormclean.dorm_cleaning_management.controller;

import com.dormclean.dorm_cleaning_management.dto.cleaningCode.GetCleaningCodeResponseDto;
import com.dormclean.dorm_cleaning_management.dto.cleaningCode.RegistrationCleaningCodeRequestDto;
import com.dormclean.dorm_cleaning_management.service.cleaningCode.CleaningCodeService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/api")
@Tag(name = "청소 인증 코드 API", description = "청소 인증 코드 등록 및 검증 관련 API")
public class CleaningCodeController {
    private final CleaningCodeService cleaningCodeService;

    @PostMapping("/cleaning-code/registration")
    public void registration(@Valid @RequestBody RegistrationCleaningCodeRequestDto dto) {
        cleaningCodeService.registration(dto);
    }

    @GetMapping("/cleaning-code/get-code")
    public ResponseEntity<GetCleaningCodeResponseDto> getAllCleaningCode() {
        GetCleaningCodeResponseDto dto = cleaningCodeService.getCleaningCode();

        if (dto == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(dto);
    }
}
