package com.dormclean.dorm_cleaning_management.controller.pages;

import org.springframework.stereotype.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;

import ch.qos.logback.core.model.Model;

@Controller
@RequiredArgsConstructor
public class AdminLoginPageController {

    @GetMapping("/login")
    public String adminLogin(Model model) {
        return "admin-login";
    }
}
