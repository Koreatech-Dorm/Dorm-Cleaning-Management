package com.dormclean.dorm_cleaning_management.entity;

import com.dormclean.dorm_cleaning_management.entity.enums.RoomStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "room", uniqueConstraints = {@UniqueConstraint(columnNames = {"dorm_id", "room_number"})})
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dorm_id", nullable = false)
    private Dorm dorm;

    @Column(nullable = false)
    private Integer floor;

    @Column(nullable = false)
    private String roomNumber;

    @Enumerated(EnumType.STRING)
    private RoomStatus roomStatus = RoomStatus.OCCUPIED;

    private Instant cleanedAt;

    @OneToOne(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private QrCode qrCode;

    @Builder
    public Room(Dorm dorm,
            Integer floor,
            String roomNumber,
            RoomStatus status,
            Instant cleanedAt) {

        this.dorm = dorm;
        this.floor = floor;
        this.roomNumber = roomNumber;
        this.roomStatus = status != null ? status : RoomStatus.OCCUPIED;
        this.cleanedAt = cleanedAt;
    }

    public void updateStatus(RoomStatus status) {
        this.roomStatus = status;
    }

    public void updateCleanedAt(Instant cleanedAt) {
        this.cleanedAt = cleanedAt;
    }

    public void assignQrCode(QrCode qrCode) {
        this.qrCode = qrCode;
    }

    public String getStatus() {
        switch (roomStatus) {
            case OCCUPIED:
                return "OCCUPIED";
            case VACANT_DIRTY:
                return "VACANT_DIRTY";
            case VACANT_CLEAN:
                return "VACANT_CLEAN";
            default:
                return "";
        }
    }

    public String getStatusLabel() {
        switch (roomStatus) {
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
