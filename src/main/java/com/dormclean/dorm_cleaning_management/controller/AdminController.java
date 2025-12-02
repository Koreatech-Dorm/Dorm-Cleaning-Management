package com.dormclean.dorm_cleaning_management.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Admin 관리 API", description = "Admin CRUD 및 로그인 API")
public class AdminController {
}
