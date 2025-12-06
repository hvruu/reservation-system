package com.practice.reservation_system.rooms;

import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Positive;

public record Room(
        @Null
        Long id,
        @NotNull
        String name,
        @NotNull
        @Positive
        Integer capacity,
        @Enumerated
        @NotNull
        RoomType type,
        @NotNull
        @Positive
        Long price,
        @Enumerated
        @NotNull
        RoomStatus status
) {
}
