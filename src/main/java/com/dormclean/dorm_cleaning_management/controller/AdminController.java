package com.dormclean.dorm_cleaning_management.controller;

import lombok.RequiredArgsConstructor;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dormclean.dorm_cleaning_management.dto.admin.AccountListResponseDto;
import com.dormclean.dorm_cleaning_management.dto.admin.AdminLoginRequestDto;
import com.dormclean.dorm_cleaning_management.entity.enums.UserRole;
import com.dormclean.dorm_cleaning_management.service.admin.UserService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/admin/api")
@RequiredArgsConstructor
@Tag(name = "유저 관리 API", description = "유저 생성 및 삭제 처리 API")
public class AdminController {
    private final UserService userService;

    // ADMIN 계정 조회
    @GetMapping("/accounts")
    public ResponseEntity<List<AccountListResponseDto>> getAdminAccounts() {
        List<AccountListResponseDto> dtoList = userService.AllAdminAccounts(UserRole.ADMIN);
        return ResponseEntity.ok(dtoList);
    }

    // 계정 등록
    @PostMapping("/signup")
    public String signup(@Valid @RequestBody AdminLoginRequestDto dto) {
        userService.create(dto.username(), dto.password());

        return "redirect:/";
    }

    // 계정 삭제
    @DeleteMapping("/delete/account")
    public ResponseEntity<Void> deleteAccount(
            @Valid @RequestParam("username") String username,
            @Valid @RequestParam("password") String password) {
        userService.delete(username, password);

        return ResponseEntity.ok().build();
    }
}
