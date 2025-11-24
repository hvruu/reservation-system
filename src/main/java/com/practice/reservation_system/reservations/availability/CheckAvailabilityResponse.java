package com.practice.reservation_system.reservations.availability;

public record CheckAvailabilityResponse (
        String message,
        AvailabilityStatus status
) {
}
