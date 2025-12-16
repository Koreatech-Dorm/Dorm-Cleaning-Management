package com.dormclean.dorm_cleaning_management.controller.pages;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class SectionPageController {
    @GetMapping("/section")
    public String dormManager(Model model) {
        return "section";
    }
}
