package com.practice.reservation_system.reservations;

import com.practice.reservation_system.rooms.RoomMapper;
import com.practice.reservation_system.rooms.RoomRepository;
import com.practice.reservation_system.users.UserMapper;
import com.practice.reservation_system.users.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class ReservationMapper {

    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final UserMapper userMapper;
    private final RoomMapper roomMapper;

    public ReservationMapper(UserRepository userRepository, RoomRepository roomRepository, UserMapper userMapper, RoomMapper roomMapper) {
        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
        this.userMapper = userMapper;
        this.roomMapper = roomMapper;
    }

    public Reservation toDomain(
            ReservationEntity reservationEntity
    ){
        return new Reservation(
                reservationEntity.getId(),
                userMapper.toDomain(reservationEntity.getUser()),
                roomMapper.toDomain(reservationEntity.getRoom()),
                reservationEntity.getStartDate(),
                reservationEntity.getEndDate(),
                reservationEntity.getStatus()
        );
    }

    public ReservationEntity toEntity(
            ReservationSave reservation
    ){
        var reservationToMap = new ReservationEntity(
                reservation.id(),
                reservation.startDate(),
                reservation.endDate(),
                reservation.status()
        );

        reservationToMap.setUser(userRepository.findById(reservation.userId())
                .orElseThrow(() -> new EntityNotFoundException("Not found user with id=" + reservation.userId())));
        reservationToMap.setRoom(roomRepository.findById(reservation.roomId())
                .orElseThrow(() -> new EntityNotFoundException("Not found room with id=" + reservation.roomId())));

        return reservationToMap;
    }
}
