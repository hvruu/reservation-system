package com.practice.reservation_system;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository repository;
    private static final Logger log =
            LoggerFactory.getLogger(ReservationService.class);


    public ReservationService(ReservationRepository repository) {
        this.repository = repository;
    }

    public Reservation createReservation(Reservation reservationToCreate) {
        if(reservationToCreate.status() != null){
            throw new IllegalArgumentException("Status should be EMPTY");
        }

        if(!reservationToCreate.endDate().isAfter(reservationToCreate.startDate())){
            throw new IllegalArgumentException("Start date must be 1 day earlier than end date");
        }

        var entityToSave = new ReservationEntity(
                null,
                reservationToCreate.userId(),
                reservationToCreate.roomId(),
                reservationToCreate.startDate(),
                reservationToCreate.endDate(),
                ReservationStatus.PENDING
        );

        var savedEntity = repository.save(entityToSave);

        return toDomainReservation(savedEntity);
    }

    public Reservation getReservationById(Long id) {

        ReservationEntity reservationEntity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Not found reservation with id = " + id));

        return toDomainReservation(reservationEntity);
    }

    public List<Reservation> findAllReservations(){
        List<ReservationEntity> allEntities = repository.findAll();
        List<Reservation> reservationList = allEntities.stream()
                .map(it -> toDomainReservation(it)
                )
                .toList();

        return reservationList;
    }

    public Reservation updateReservation(Long id, Reservation reservationToUpdate) {
        ReservationEntity reservationEntity = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Not found reservation with id = " + id));

        if(reservationEntity.getStatus() != ReservationStatus.PENDING){
            throw new IllegalStateException("Cannot modify reservation. Status = " + reservationEntity.getStatus());
        }

        if(!reservationToUpdate.endDate().isAfter(reservationToUpdate.startDate())){
            throw new IllegalArgumentException("Start date must be 1 day earlier than end date");
        }

        var reservationToSave = new ReservationEntity(
                reservationEntity.getId(),
                reservationToUpdate.userId(),
                reservationToUpdate.roomId(),
                reservationToUpdate.startDate(),
                reservationToUpdate.endDate(),
                ReservationStatus.PENDING
        );

        var updatedReservation = repository.save(reservationToSave);

        return toDomainReservation(updatedReservation);
    }

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

        var isConflict = isReservationConflict(reservationEntity);
        if(isConflict){
            throw new IllegalStateException("Cannot approve reservation because of conflict");
        }

        reservationEntity.setStatus(ReservationStatus.APPROVED);

        repository.save(reservationEntity);

        return toDomainReservation(reservationEntity);

    }

    private boolean isReservationConflict(
            ReservationEntity reservation
    ){
        List<ReservationEntity> reservationEntityList = repository.findAll();

        for( ReservationEntity existingReservation: reservationEntityList){
            if(reservation.getId().equals(existingReservation.getId())){
                continue;
            }
            if(!reservation.getRoomId().equals(existingReservation.getRoomId())){
                continue;
            }
            if(!existingReservation.getStatus().equals(ReservationStatus.APPROVED)){
                continue;
            }
            if(reservation.getStartDate().isBefore(existingReservation.getEndDate())
            && existingReservation.getStartDate().isBefore(reservation.getEndDate())){
                return true;
            }
        }
        return false;
    }

    private Reservation toDomainReservation(
            ReservationEntity reservationEntity
    ){
        return new Reservation(
                reservationEntity.getId(),
                reservationEntity.getUserId(),
                reservationEntity.getRoomId(),
                reservationEntity.getStartDate(),
                reservationEntity.getEndDate(),
                reservationEntity.getStatus()
        );
    }
}
