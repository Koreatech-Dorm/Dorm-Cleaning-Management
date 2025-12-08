package com.dormclean.dorm_cleaning_management.controller.pages;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class IndexController {

    @GetMapping
    public String adminIndex() {
        return "index";
    }

    @GetMapping("/")
    public String redirectToAdmin() {
        return "redirect:/admin";
    }
}
