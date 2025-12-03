package com.dormclean.dorm_cleaning_management.controller;

import com.dormclean.dorm_cleaning_management.dto.CleaningCodeDto;
import com.dormclean.dorm_cleaning_management.dto.GetCleaningCodeResponseDto;
import com.dormclean.dorm_cleaning_management.dto.RegistrationCleaningCodeRequestDto;
import com.dormclean.dorm_cleaning_management.entity.CleaningCode;
import com.dormclean.dorm_cleaning_management.repository.CleaningCodeRepository;
import com.dormclean.dorm_cleaning_management.service.CleaningCodeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cleaning-code")
@Tag(name = "청소 인증 코드 API", description = "청소 인증 코드 등록 및 검증 관련 API")
public class CleaningCodeController {
    private final CleaningCodeService cleaningCodeService;
    private final CleaningCodeRepository cleaningCodeRepository;

    @PostMapping("/registration")
    public void registration(@RequestBody RegistrationCleaningCodeRequestDto dto) {
        cleaningCodeService.registration(dto.cleaningCode());
    }

    @PostMapping("/use-code")
    public ResponseEntity<String> useCode(@RequestBody CleaningCodeDto dto) {
        cleaningCodeService.useCleaningCode(dto.cleaningCode());

        return ResponseEntity.ok("코드 인증되었습니다.");
    }

    @GetMapping("/get-code")
    public ResponseEntity<GetCleaningCodeResponseDto> getAllCleaningCode() {
        CleaningCode cleaningCode = cleaningCodeRepository.findAll()
                .stream()
                .findFirst()
                .orElse(null);

        if (cleaningCode == null) {
            return ResponseEntity.notFound().build();
        }

        GetCleaningCodeResponseDto dto = new GetCleaningCodeResponseDto(
                cleaningCode.getCleaningCode()
        );

        return ResponseEntity.ok(dto);
    }
}
