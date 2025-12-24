package com.dormclean.dorm_cleaning_management.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
public class HealthController {
    @GetMapping("health")
    public ResponseEntity<Void> health(){
        return ResponseEntity.ok().build();
    }
}
