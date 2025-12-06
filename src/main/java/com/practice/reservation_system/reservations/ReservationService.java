package com.practice.reservation_system.reservations;

import com.practice.reservation_system.reservations.availability.ReservationAvailabilityService;
import com.practice.reservation_system.rooms.RoomEntity;
import com.practice.reservation_system.rooms.RoomRepository;
import com.practice.reservation_system.users.UserEntity;
import com.practice.reservation_system.users.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ReservationService {

    private final ReservationRepository repository;
    private static final Logger log =
            LoggerFactory.getLogger(ReservationService.class);
    private final ReservationMapper mapper;
    private final ReservationAvailabilityService availabilityService;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;


    public ReservationService(ReservationRepository repository, ReservationMapper mapper, ReservationAvailabilityService availabilityService, RoomRepository roomRepository, UserRepository userRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.availabilityService = availabilityService;
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
    }

    public Reservation createReservation(ReservationSave reservationToCreate) {
        if(reservationToCreate.status() != null){
            throw new IllegalArgumentException("Status should be EMPTY");
        }

        if(!reservationToCreate.endDate().isAfter(reservationToCreate.startDate())){
            throw new IllegalArgumentException("Start date must be 1 day earlier than end date");
        }

        var roomToReserve = roomRepository.findById(reservationToCreate.roomId())
                .orElseThrow(() -> new NoSuchElementException("Not found room with id=" + reservationToCreate.id()));

        if(!availabilityService.isReservationAvailable(roomToReserve, reservationToCreate.startDate(), reservationToCreate.endDate())){
            throw new IllegalStateException("Room is not available for the selected dates");
        }

        var entityToSave = mapper.toEntity(reservationToCreate);
        entityToSave.setStatus(ReservationStatus.PENDING);

        var savedEntity = repository.save(entityToSave);

        return mapper.toDomain(savedEntity);
    }

    public Reservation getReservationById(Long id) {

        ReservationEntity reservationEntity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Not found reservation with id = " + id));

        return mapper.toDomain(reservationEntity);
    }

    public List<Reservation> searchAllByFilter(
            ReservationSearchFilter filter
    ){
        int pageSize = filter.pageSize() != null ? filter.pageSize() : 5;
        int pageNumber = filter.pageNum() != null ? filter.pageNum() : 0;

        var pageable = Pageable.ofSize(pageSize).withPage(pageNumber);

        RoomEntity room = null;
        UserEntity user = null;
        if(filter.roomId() != null){
            room = roomRepository.findById(filter.roomId())
                    .orElseThrow(() -> new EntityNotFoundException("Room not found"));
        }
        if(filter.userId() != null){
            user = userRepository.findById(filter.userId())
                    .orElseThrow(() -> new EntityNotFoundException("User not found"));
        }

        List<ReservationEntity> allEntities = repository.findAllByFilter(room, user, pageable);


        return allEntities.stream()
                .map(mapper::toDomain)
                .toList();
    }

    public Reservation updateReservation(Long id, ReservationSave reservationToUpdate) {
        ReservationEntity reservationEntity = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Not found reservation with id = " + id));

        if(reservationEntity.getStatus() != ReservationStatus.PENDING){
            throw new IllegalStateException("Cannot modify reservation. Status = " + reservationEntity.getStatus());
        }

        if(!reservationToUpdate.endDate().isAfter(reservationToUpdate.startDate())){
            throw new IllegalArgumentException("Start date must be 1 day earlier than end date");
        }

        var reservationToSave = mapper.toEntity(reservationToUpdate);
        reservationToSave.setId(reservationEntity.getId());
        reservationToSave.setStatus(ReservationStatus.PENDING);

        var updatedReservation = repository.save(reservationToSave);

        return mapper.toDomain(updatedReservation);
    }

    @Transactional
    public void cancelReservation(Long id) {
        var reservation = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Not found reservation with id = " + id));

        if(reservation.getStatus().equals(ReservationStatus.APPROVED)){
            throw new IllegalStateException("Reservation already approved. Please contact manager");
        }
        if(reservation.getStatus().equals(ReservationStatus.CANCELLED)){
            throw new IllegalStateException("Cannot cancel reservation. Reservation already Cancelled");
        }

        repository.setStatus(id, ReservationStatus.CANCELLED);
        log.info("Reservation (id:{}) successfully cancelled", id);
    }

    public Reservation approveReservation(Long id) {
        ReservationEntity reservationEntity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Not found reservation with id = " + id));

        if(reservationEntity.getStatus() != ReservationStatus.PENDING){
            throw new IllegalStateException("Status should be PENDING");
        }

        var isAvailableToApprove = availabilityService.isReservationAvailable(
                reservationEntity.getRoom(),
                reservationEntity.getStartDate(),
                reservationEntity.getEndDate()
        );
        if(!isAvailableToApprove){
            throw new IllegalStateException("Cannot approve reservation because of conflict");
        }

        reservationEntity.setStatus(ReservationStatus.APPROVED);

        repository.save(reservationEntity);

        return mapper.toDomain(reservationEntity);

    }
}
