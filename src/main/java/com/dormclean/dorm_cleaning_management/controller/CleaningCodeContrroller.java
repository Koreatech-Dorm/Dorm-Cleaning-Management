package com.dormclean.dorm_cleaning_management.controller;

import com.dormclean.dorm_cleaning_management.dto.RegistrationCleaningCodeRequestDto;
import com.dormclean.dorm_cleaning_management.service.CleaningCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cleaning-code")
public class CleaningCodeContrroller {
    private final CleaningCodeService cleaningCodeService;

    @PostMapping("/registration")
    public void registration(@RequestBody RegistrationCleaningCodeRequestDto dto) {
        cleaningCodeService.registration(dto.dormCode(), dto.cleaningCode());
    }
}
