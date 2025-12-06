package com.practice.reservation_system.reservations.availability;

import com.practice.reservation_system.reservations.ReservationRepository;
import com.practice.reservation_system.reservations.ReservationStatus;
import com.practice.reservation_system.rooms.RoomEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReservationAvailabilityService {
    private final ReservationRepository repository;
    private static final Logger log =
            LoggerFactory.getLogger(ReservationAvailabilityService.class);

    public ReservationAvailabilityService(ReservationRepository repository) {
        this.repository = repository;
    }


    public boolean isReservationAvailable(
            RoomEntity room,
            LocalDate startDate,
            LocalDate endDate
    ){
        List<Long> conflictingIds = repository.findConflictReservationIds(
                room,
                startDate,
                endDate,
                ReservationStatus.APPROVED
        );

        if(!endDate.isAfter(startDate)){
            throw new IllegalArgumentException("Start date must be 1 day earlier than end date");
        }

        if(conflictingIds.isEmpty()){
            return true;
        }
        log.info("conflict with ids = {}", conflictingIds);
        return false;
    }
}