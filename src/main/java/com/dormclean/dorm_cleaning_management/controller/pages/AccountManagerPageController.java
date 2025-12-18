package com.dormclean.dorm_cleaning_management.controller.pages;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dormclean.dorm_cleaning_management.entity.AdminUser;
import com.dormclean.dorm_cleaning_management.entity.enums.UserRole;
import com.dormclean.dorm_cleaning_management.repository.UserRepository;

import org.springframework.ui.Model;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AccountManagerPageController {
    private final UserRepository userRepository;

    @GetMapping("/account-manager")
    public String adminLogin(Model model) {
        AdminUser superAdmin = userRepository.findByRole(UserRole.SUPERADMIN)
                .orElseThrow(() -> new RuntimeException("SUPERADMIN not found"));
        model.addAttribute("superAdmin", superAdmin);
        return "account-manager";
    }
}
