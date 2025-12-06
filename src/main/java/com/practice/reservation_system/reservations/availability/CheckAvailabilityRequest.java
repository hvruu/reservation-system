package com.practice.reservation_system.reservations.availability;

import com.practice.reservation_system.reservations.ReservationEntity;
import com.practice.reservation_system.rooms.RoomEntity;

import java.time.LocalDate;

public record CheckAvailabilityRequest(
        RoomEntity room,
        LocalDate startDate,
        LocalDate endDate
) {
}
