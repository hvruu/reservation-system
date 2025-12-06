package com.practice.reservation_system.rooms;

import org.springframework.web.bind.annotation.RequestParam;

public record RoomSearchFilter(
         Long id,
         Integer capacity,
         RoomType type,
         Long priceStart,
         Long priceEnd,
         RoomStatus status,
         Integer pageSize,
         Integer pageNum
) {
}
