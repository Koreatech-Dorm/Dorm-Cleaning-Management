package com.dormclean.dorm_cleaning_management.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "cleaning_code")
public class CleaningCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 10)
    private String cleaningCode;

    @Builder
    public CleaningCode(String cleaningCode) {
        this.cleaningCode = cleaningCode;
    }

    public void updateCode(String cleaningCode) {
        this.cleaningCode = cleaningCode;
    }
}
