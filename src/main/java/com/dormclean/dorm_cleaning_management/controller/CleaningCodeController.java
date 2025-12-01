package com.dormclean.dorm_cleaning_management.controller;

import com.dormclean.dorm_cleaning_management.dto.CleaningCodeDto;
import com.dormclean.dorm_cleaning_management.dto.RegistrationCleaningCodeRequestDto;
import com.dormclean.dorm_cleaning_management.service.CleaningCodeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cleaning-code")
@Tag(name = "청소 인증 코드 API", description = "청소 인증 코드 등록 및 검증 관련 API")
public class CleaningCodeController {
    private final CleaningCodeService cleaningCodeService;

    @PostMapping("/registration")
    public void registration(@RequestBody RegistrationCleaningCodeRequestDto dto) {
        cleaningCodeService.registration(dto.dormCode(), dto.cleaningCode());
    }

    @PostMapping("/use-code")
    public ResponseEntity<String> useCode(@RequestBody CleaningCodeDto dto) {
        cleaningCodeService.useCleaningCode(dto.cleaningCode(), dto.dormCode());

        return ResponseEntity.ok("코드 인증되었습니다.");
    }
}
