package com.dormclean.dorm_cleaning_management.entity;

import com.dormclean.dorm_cleaning_management.entity.enums.CheckOutStatus;
import com.dormclean.dorm_cleaning_management.entity.enums.CleanStatus;
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
    @JoinColumn(name = "dorm_id", nullable = false)
    private Dorm dorm;

    private Integer floor;

    private String roomNumber;

    @Enumerated(EnumType.STRING)
    private CheckOutStatus checkOutStatus = CheckOutStatus.OCCUPIED;

    @Enumerated(EnumType.STRING)
    private  CleanStatus cleanStatus = CleanStatus.UNCLEANED;

    private Instant cleanedAt;

    @OneToOne(mappedBy = "room", fetch = FetchType.LAZY)
    private QrCode qrCode;

    @Builder
    public Room(Dorm dorm,
            Integer floor,
            String roomNumber,
            CheckOutStatus checkOutStatus,
            CleanStatus cleanStatus,
            Instant cleanedAt) {

        this.dorm = dorm;
        this.floor = floor;
        this.roomNumber = roomNumber;
        this.checkOutStatus = checkOutStatus != null ? checkOutStatus : CheckOutStatus.OCCUPIED;
        this.cleanStatus = cleanStatus != null ? cleanStatus : CleanStatus.UNCLEANED;
        this.cleanedAt = cleanedAt;
    }

    public void updateCleanStatus(CleanStatus cleanStatus) {
        this.cleanStatus = cleanStatus;
    }
    public void updateCheckOutStatus(CheckOutStatus checkOutStatus) {
        this.checkOutStatus = checkOutStatus;
    }
    public void updateCleanedAt(Instant cleanedAt) {
        this.cleanedAt = cleanedAt;
    }
    public void assignQrCode(QrCode qrCode) {
        this.qrCode = qrCode;
    }
}
