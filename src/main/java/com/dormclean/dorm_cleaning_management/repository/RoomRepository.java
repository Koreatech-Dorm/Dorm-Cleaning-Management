package com.dormclean.dorm_cleaning_management.repository;

import com.dormclean.dorm_cleaning_management.dto.room.RoomListResponseDto;
import com.dormclean.dorm_cleaning_management.entity.enums.RoomStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dormclean.dorm_cleaning_management.entity.Room;
import com.dormclean.dorm_cleaning_management.entity.Dorm;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

  // 호실 생성 시: 특정 생활관의 호실 중복 체크
  boolean existsByDormAndRoomNumber(Dorm dorm, String roomNumber);

  // 특정 방 조회
  Optional<Room> findById(Long roomId);

  // 특정 기숙사의 전체 방 조회
  List<Room> findByDorm(Dorm dorm);

  // 특정 기숙사의 방 조회
  @Query("SELECT r FROM Room r JOIN FETCH r.dorm d WHERE d.dormCode = :dormCode AND r.roomNumber = :roomNumber")
  Optional<Room> findByDormCodeAndRoomNumber(
      @Param("dormCode") String dormCode,
      @Param("roomNumber") String roomNumber);

  @Modifying(clearAutomatically = true)
  @Query("""
      update Room r
      set r.roomStatus = :status,
          r.cleanedAt = :now
      where r.dorm = :dorm
        and r.roomNumber in :roomNumbers
      """)
  int bulkStatusUpdate(
      @Param("dorm") Dorm dorm,
      @Param("roomNumbers") List<String> roomNumbers,
      @Param("status") RoomStatus status,
      @Param("now") Instant now);

  @Query("""
      select new com.dormclean.dorm_cleaning_management.dto.room.RoomListResponseDto(
          r.dorm.dormCode,
          r.floor,
          r.roomNumber,
          r.roomStatus,
          r.cleanedAt,
          r.checkInAt,
          r.checkOutAt
      )
      from Room r
      """)
  List<RoomListResponseDto> findAllRoomsDto();

  @Query("""
      select new com.dormclean.dorm_cleaning_management.dto.room.RoomListResponseDto(
          r.dorm.dormCode,
          r.floor,
          r.roomNumber,
          r.roomStatus,
          r.cleanedAt,
          r.checkInAt,
          r.checkOutAt
          )
       from Room r
       where r.dorm.dormCode = :dormCode
      """)
  List<RoomListResponseDto> findRoomByDormCode(@Param("dormCode") String dormCode);

  @Query("""
      select new com.dormclean.dorm_cleaning_management.dto.room.RoomListResponseDto(
          r.dorm.dormCode,
          r.floor,
          r.roomNumber,
          r.roomStatus,
          r.cleanedAt,
          r.checkInAt,
          r.checkOutAt
      )
      from Room r
      where r.dorm.dormCode = :dormCode
        and r.floor = :floor
      """)
  List<RoomListResponseDto> findRoomByDormCodeAndFloor(
      @Param("dormCode") String dormCode,
      @Param("floor") Integer floor);

  @Query("""
      select r
      from Room r join r.dorm d where d.dormCode = :dormCode and r.roomNumber = :roomNumber
      """)
  Room findRoomByDormCodeAndRoomNumber(
      @Param("dormCode") String dormCode,
      @Param("roomNumber") String roomNumber);

  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Query("""
          delete from Room r
          where r.dorm.dormCode = :dormCode
          and r.roomNumber in :roomNumbers
      """)
  int deleteByDormCodeAndRoomNumbers(
      @Param("dormCode") String dormCode,
      @Param("roomNumbers") List<String> roomNumbers);

  @Query("""
      select r
      from Room r
      join fetch r.dorm d
      left join fetch r.qrCode q
      where d.dormCode in :dormCodes
      """)
  List<Room> findAllRoomsWithDormAndQrByDormCodes(
      @Param("dormCodes") List<String> dormCodes);

  @Modifying(clearAutomatically = true)
  @Query("UPDATE Room r SET r.roomStatus = 'OCCUPIED' " +
      "WHERE r.roomNumber = :roomNumber " +
      "AND r.dorm.dormCode = :dormCode " +
      "AND r.roomStatus = 'READY'")
  int updateStatusToCheckIn(
      @Param("dormCode") String dormCode,
      @Param("roomNumber") String roomNumber);

  @Modifying(clearAutomatically = true)
  @Query("UPDATE Room r SET r.roomStatus = 'VACANT' " +
      "WHERE r.roomNumber = :roomNumber " +
      "AND r.dorm.dormCode = :dormCode " +
      "AND r.roomStatus = 'OCCUPIED'")
  int updateStatusToCheckOut(
      @Param("dormCode") String dormCode,
      @Param("roomNumber") String roomNumber);

  @Modifying(clearAutomatically = true)
  @Query("UPDATE Room r SET r.roomStatus = 'READY' " +
      "WHERE r.roomNumber = :roomNumber " +
      "AND r.dorm.dormCode = :dormCode " +
      "AND r.roomStatus = 'VACANT'")
  int updateStatusToCleaned(
      @Param("dormCode") String dormCode,
      @Param("roomNumber") String roomNumber);
}
