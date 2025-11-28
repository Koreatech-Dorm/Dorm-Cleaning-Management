package com.dormclean.dorm_cleaning_management.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cleaning_code")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CleaningCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dorm_id", nullable = false, unique = true)
    private Dorm dorm;

    @Column(nullable = false, length = 10)
    private String code;

    @Builder
    public CleaningCode(Dorm dorm, String code) {
        this.dorm = dorm;
        this.code = code;
    }

    public void updateCode(String code){
        this.code = code;
    }
}
