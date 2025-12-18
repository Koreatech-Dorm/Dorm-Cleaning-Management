package com.dormclean.dorm_cleaning_management.entity;

import com.dormclean.dorm_cleaning_management.entity.enums.RoomStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "room", uniqueConstraints = { @UniqueConstraint(columnNames = { "dorm_id", "room_number" }) })
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
    private RoomStatus roomStatus = RoomStatus.READY;

    private Instant cleanedAt;
    private Instant checkInAt;
    private Instant checkOutAt;

    @OneToOne(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private QrCode qrCode;

    @Builder
    public Room(Dorm dorm,
            Integer floor,
            String roomNumber,
            RoomStatus status,
            Instant cleanedAt, Instant checkInAt, Instant checkOutAt) {

        this.dorm = dorm;
        this.floor = floor;
        this.roomNumber = roomNumber;
        this.roomStatus = status != null ? status : RoomStatus.READY;
        this.cleanedAt = cleanedAt;
        this.checkInAt = checkInAt;
        this.checkOutAt = checkOutAt;
    }

    public void updateStatus(RoomStatus status) {
        this.roomStatus = status;
    }

    public void updateCleanedAt(Instant cleanedAt) {
        this.cleanedAt = cleanedAt;
    }

    public void updateCheckInAt(Instant checkInAt) {
        this.checkInAt = checkInAt;
    }

    public void updateCheckOutAt(Instant checkOutAt) {
        this.checkOutAt = checkOutAt;
    }

    public void checkIn() {
        if (this.roomStatus == RoomStatus.READY) {
            this.roomStatus = RoomStatus.OCCUPIED;
            this.checkInAt = Instant.now();
            this.checkOutAt = null;
            this.cleanedAt = null;
        }
    }

    public void checkOut() {
        if (this.roomStatus == RoomStatus.OCCUPIED) {
            this.roomStatus = RoomStatus.VACANT;
            this.checkOutAt = Instant.now();
        }
    }

    public void clean() {
        if (this.getRoomStatus() == RoomStatus.VACANT) {
            this.updateStatus(RoomStatus.READY);
            this.cleanedAt = Instant.now();
        }
    }
}
