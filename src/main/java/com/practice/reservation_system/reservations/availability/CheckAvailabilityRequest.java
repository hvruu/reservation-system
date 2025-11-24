package com.practice.reservation_system.reservations.availability;

import java.time.LocalDate;

public record CheckAvailabilityRequest(
        Long roomId,
        LocalDate startDate,
        LocalDate endDate
) {
}
