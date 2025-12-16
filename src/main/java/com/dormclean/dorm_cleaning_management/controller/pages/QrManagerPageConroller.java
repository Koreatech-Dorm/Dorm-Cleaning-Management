package com.dormclean.dorm_cleaning_management.controller.pages;

import com.dormclean.dorm_cleaning_management.entity.Dorm;
import com.dormclean.dorm_cleaning_management.repository.DormRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class QrManagerPageConroller {
    private final DormRepository dormRepository;

    @GetMapping("/qr-manager")
    public String createQR(Model model) {
        List<Dorm> dormList = dormRepository.findAllByOrderByDormCodeAsc();

        model.addAttribute("dorms", dormList);

        return "qr-manager";
    }
}
