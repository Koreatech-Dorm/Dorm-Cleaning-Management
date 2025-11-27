package com.dormclean.dorm_cleaning_management.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "room")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dorm_id", nullable = false)
    private Dorm dorm;

    private Integer floor;

    private String roomNumber;

    @Enumerated(EnumType.STRING)
    private RoomStatus status = RoomStatus.OCCUPIED;

    private Instant cleanedAt;

    @Builder
    public Room(Dorm dorm,
            Integer floor,
            String roomNumber,
            RoomStatus status,
            Instant cleanedAt) {

        this.dorm = dorm;
        this.floor = floor;
        this.roomNumber = roomNumber;
        this.status = status != null ? status : RoomStatus.OCCUPIED;
        this.cleanedAt = cleanedAt;
    }

    public enum RoomStatus {
        VACANT_DIRTY,
        VACANT_CLEAN,
        OCCUPIED
    }

    // 필요한 상태 변경 메서드만 제공
    public void updateStatus(RoomStatus status) {
        this.status = status;
    }

    // 상태 레이블 반환
    public void updateCleanedAt(Instant cleanedAt) {
        this.cleanedAt = cleanedAt;
    }

    public String getStatusLabel() {
        switch (status) {
            case OCCUPIED:
                return "재실";
            case VACANT_DIRTY:
                return "공실 (청소 필요)";
            case VACANT_CLEAN:
                return "공실 (청소 완료)";
            default:
                return "";
        }
    }

}
