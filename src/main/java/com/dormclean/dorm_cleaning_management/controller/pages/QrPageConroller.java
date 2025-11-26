package com.dormclean.dorm_cleaning_management.controller.pages;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class QrPageConroller {
    @GetMapping("/create-qr")
    public String createQR(){
        return "create-qr";
    }
}
