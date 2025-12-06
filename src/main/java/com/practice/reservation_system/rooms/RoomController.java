package com.practice.reservation_system.rooms;

import com.practice.reservation_system.users.User;
import com.practice.reservation_system.users.UserSearchFilter;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/room")
public class RoomController {
    private final RoomService service;
    private static final Logger log =
            LoggerFactory.getLogger(RoomController.class);

    public RoomController(RoomService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Room>> getRoomByFilter(
            @RequestParam(name="id", required = false) Long id,
            @RequestParam(name="capacity", required = false) Integer capacity,
            @RequestParam(name="type", required = false) RoomType type,
            @RequestParam(name="priceStart", required = false) Long priceStart,
            @RequestParam(name="priceEnd", required = false) Long priceEnd,
            @RequestParam(name="status", required = false) RoomStatus status,
            @RequestParam(name="pageSize", required = false) Integer pageSize,
            @RequestParam(name="pageNum", required = false) Integer pageNum
    ){
        log.info("called method getRoomByFilter");

        var filter = new RoomSearchFilter(
                id,
                capacity,
                type,
                priceStart,
                priceEnd,
                status,
                pageSize,
                pageNum
        );

        return ResponseEntity.ok(service.findByFilter(filter));
    }

    @PostMapping
    public ResponseEntity<Room> createRoom(
            @RequestBody @Valid Room roomToCreate
    ){
        log.info("called method createRoom");

        return ResponseEntity.ok(service.createRoom(roomToCreate));

    }
}
