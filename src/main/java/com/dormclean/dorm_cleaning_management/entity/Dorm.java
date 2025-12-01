package com.dormclean.dorm_cleaning_management.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "dorm", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Room> rooms = new ArrayList<>();

    @OneToOne(mappedBy = "dorm", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private CleaningCode cleaningCode;

    @Builder
    public Dorm(String dormCode, String dormName) {
        this.dormCode = dormCode;
        this.dormName = dormName;
    }

    public void setDormName(String newName) {
        this.dormName = newName;
    }
}
