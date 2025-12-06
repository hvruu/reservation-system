package com.practice.reservation_system.rooms;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoomRepository extends JpaRepository<RoomEntity, Long> {
    @Query(
            "SELECT r FROM RoomEntity r WHERE " +
                    "(:id IS NULL OR r.id = :id) AND " +
                    "(:capacity IS NULL OR r.capacity = :capacity) AND " +
                    "(:type IS NULL OR r.type = :type) AND " +
                    "(:priceStart IS NULL OR r.price >= :priceStart) AND " +
                    "(:priceEnd IS NULL OR r.price <= :priceEnd) AND " +
                    "(:status IS NULL OR r.status = :status)"
    )
    List<RoomEntity> findByFilter(
            @Param("id") Long id,
            @Param("capacity") Integer capacity,
            @Param("type") RoomType type,
            @Param("priceStart") Long priceStart,
            @Param("priceEnd") Long priceEnd,
            @Param("status") RoomStatus status,
            Pageable pageable
    );

}
