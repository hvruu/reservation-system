package com.practice.reservation_system.rooms;

public class RoomMapper {

    public Room toDomain(RoomEntity entity){
        return new Room(
                entity.getId(),
                entity.getName(),
                entity.getCapacity(),
                entity.getType(),
                entity.getPrice(),
                entity.getStatus()
        );
    }

    public RoomEntity toEntity(Room room){
        return new RoomEntity(
                room.id(),
                room.name(),
                room.capacity(),
                room.type(),
                room.price(),
                room.status()
        );
    }
}
