package com.dormclean.dorm_cleaning_management.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "room")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dormitory_id", nullable = false)
    private Dorm dormitory;

    private Integer floor;

    private String roomNumber;

    @Enumerated(EnumType.STRING)
    private RoomStatus status = RoomStatus.OCCUPIED;

    private Instant cleanedAt;

    @Builder
    public Room(Dorm dormitory,
            Integer floor,
            String roomNumber,
            RoomStatus status,
            Instant cleanedAt) {

        this.dormitory = dormitory;
        this.floor = floor;
        this.roomNumber = roomNumber;
        this.status = status != null ? status : RoomStatus.OCCUPIED;
        this.cleanedAt = cleanedAt;
    }

    public enum RoomStatus {
        OCCUPIED,
        CLEANED,
        DIRTY
    }

    // 필요한 상태 변경 메서드만 제공
    public void updateStatus(RoomStatus status) {
        this.status = status;
    }

    public void updateCleanedAt(Instant cleanedAt) {
        this.cleanedAt = cleanedAt;
    }
}
