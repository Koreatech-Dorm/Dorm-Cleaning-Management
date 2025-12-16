package com.dormclean.dorm_cleaning_management.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.dormclean.dorm_cleaning_management.dto.admin.AdminLoginResponseDto;
import com.dormclean.dorm_cleaning_management.service.admin.AuthService;
import com.dormclean.dorm_cleaning_management.dto.admin.AdminLoginRequestDto;

@RestController
@RequestMapping("/admin/api")
@RequiredArgsConstructor
@Tag(name = "Admin 관리 API", description = "Admin CRUD 및 로그인 API")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AdminLoginResponseDto> login(@Valid @RequestBody AdminLoginRequestDto dto,
            HttpSession session) {
        AdminLoginResponseDto adminLoginResponseDto = authService.login(dto);

        session.setAttribute("ADMIN_ID", adminLoginResponseDto.adminId());

        session.setMaxInactiveInterval(1800); // 세션 유지 시간 30분

        return ResponseEntity.ok(adminLoginResponseDto);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session, HttpServletResponse response) {
        session.invalidate();

        Cookie cookie = new Cookie("JSESSIONID", null);
        cookie.setMaxAge(0); // 수명을 0으로 -> 즉시 삭제
        cookie.setPath("/"); // 모든 경로에서 삭제
        response.addCookie(cookie);

        return ResponseEntity.ok("로그아웃 되었습니다.");
    }
}
