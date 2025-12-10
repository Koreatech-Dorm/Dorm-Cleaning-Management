package com.dormclean.dorm_cleaning_management.controller;

import com.dormclean.dorm_cleaning_management.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.dormclean.dorm_cleaning_management.dto.AdminLoginResponseDto;
import com.dormclean.dorm_cleaning_management.dto.AdminLoginRequestDto;

@RestController
@RequestMapping("/admin/api")
@RequiredArgsConstructor
@Tag(name = "Admin 관리 API", description = "Admin CRUD 및 로그인 API")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AdminLoginResponseDto> login(@RequestBody AdminLoginRequestDto dto, HttpSession session) {
        AdminLoginResponseDto adminLoginResponseDto = authService.login(dto);

        session.setAttribute("ADMIN_ID", adminLoginResponseDto.adminId());

        session.setMaxInactiveInterval(1800); // 세션 유지 시간 30분

        return ResponseEntity.ok(adminLoginResponseDto);
    }
}
