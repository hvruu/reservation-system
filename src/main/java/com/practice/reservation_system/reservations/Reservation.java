package com.practice.reservation_system.reservations;

import com.practice.reservation_system.rooms.Room;
import com.practice.reservation_system.users.User;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record Reservation(
        @Null
        Long id,
        @NotNull
        User user,
        @NotNull
        Room room,
        @FutureOrPresent
        @NotNull
        LocalDate startDate,
        @FutureOrPresent
        @NotNull
        LocalDate endDate,
        ReservationStatus status
) {
}
