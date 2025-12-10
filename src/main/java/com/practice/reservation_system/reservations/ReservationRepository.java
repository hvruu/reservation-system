package com.practice.reservation_system.reservations;

import com.practice.reservation_system.rooms.RoomEntity;
import com.practice.reservation_system.users.UserEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {

    @Modifying
    @Query("update ReservationEntity r set r.status = :status where r.id = :id")
    void setStatus(@Param("id") Long id, @Param("status") ReservationStatus reservationStatus);

    @Query("select r.id from ReservationEntity r where r.room = :room and :startDate < r.endDate and r.startDate < :endDate and r.status = :status")
    List<Long> findConflictReservationIds(
            @Param("room") RoomEntity room,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("status") ReservationStatus status
    );

    @EntityGraph(value = "ReservationWithRelations")
    @Query("select r from ReservationEntity r where (:room is null or r.room = :room) and (:user is null or r.user = :user)")
    List<ReservationEntity> findAllByFilter(
            @Param("room") RoomEntity room,
            @Param("user") UserEntity user,
            Pageable pageable
    );
}
