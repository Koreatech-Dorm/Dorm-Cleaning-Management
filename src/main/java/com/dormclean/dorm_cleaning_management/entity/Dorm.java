package com.dormclean.dorm_cleaning_management.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "dorm")
public class Dorm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String dormCode;

    private String dormName;

    @Builder
    public Dorm(String dormCode, String dormName) {
        this.dormCode = dormCode;
        this.dormName = dormName;
    }
}
