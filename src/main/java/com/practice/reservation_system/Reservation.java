package com.practice.reservation_system;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record Reservation(
        @Null
        Long id,
        @NotNull
        Long userId,
        @NotNull
        Long roomId,
        @FutureOrPresent
        @NotNull
        LocalDate startDate,
        @FutureOrPresent
        @NotNull
        LocalDate endDate,
        ReservationStatus status
) {
}
