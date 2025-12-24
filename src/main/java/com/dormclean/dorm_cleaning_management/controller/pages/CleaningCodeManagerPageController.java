package com.dormclean.dorm_cleaning_management.controller.pages;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dormclean.dorm_cleaning_management.entity.Dorm;
import com.dormclean.dorm_cleaning_management.repository.DormRepository;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class CleaningCodeManagerPageController {
    private final DormRepository dormRepository;

    @GetMapping("/cleaning-code-manager")
    public String dormManager(Model model) {
        List<Dorm> dormList = dormRepository.findAll();
        model.addAttribute("dorms", dormList);
        return "cleaning-code-manager";
    }
}
