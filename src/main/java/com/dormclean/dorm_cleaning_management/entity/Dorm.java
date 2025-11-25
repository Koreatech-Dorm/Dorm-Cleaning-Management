package com.dormclean.dorm_cleaning_management.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "dorm_room")
public class Dorm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Dorm 정보
    @Column(nullable = false, unique = true)
    private String dormCode;

    private String dormName;

    // Room 정보
    private Integer floor;

    private String roomNumber;

    @Enumerated(EnumType.STRING)
    private RoomStatus status = RoomStatus.OCCUPIED;

    private Instant cleanedAt;

    protected Dorm() {
    }

    public Dorm(Long id,
            String dormCode,
            String dormName,
            Integer floor,
            String roomNumber,
            RoomStatus status,
            Instant cleanedAt) {

        this.id = id;
        this.dormCode = dormCode;
        this.dormName = dormName;
        this.floor = floor;
        this.roomNumber = roomNumber;
        this.status = status;
        this.cleanedAt = cleanedAt;
    }

    public Dorm(String dormCode, String dormName,
            Integer floor, String roomNumber) {

        this.dormCode = dormCode;
        this.dormName = dormName;
        this.floor = floor;
        this.roomNumber = roomNumber;
        this.status = RoomStatus.OCCUPIED;
    }

    // Getter & Setter

    public Long getId() {
        return id;
    }

    public String getDormCode() {
        return dormCode;
    }

    public void setDormCode(String dormCode) {
        this.dormCode = dormCode;
    }

    public String getDormName() {
        return dormName;
    }

    public void setDormName(String dormName) {
        this.dormName = dormName;
    }

    public Integer getFloor() {
        return floor;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public RoomStatus getStatus() {
        return status;
    }

    public void setStatus(RoomStatus status) {
        this.status = status;
    }

    public Instant getCleanedAt() {
        return cleanedAt;
    }

    public void setCleanedAt(Instant cleanedAt) {
        this.cleanedAt = cleanedAt;
    }

    public enum RoomStatus {
        OCCUPIED,
        CLEANED,
        DIRTY
    }
}
