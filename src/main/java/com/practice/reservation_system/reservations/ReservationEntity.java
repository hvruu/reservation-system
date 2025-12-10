package com.practice.reservation_system.reservations;

import com.practice.reservation_system.rooms.RoomEntity;
import com.practice.reservation_system.users.UserEntity;
import jakarta.persistence.*;
import org.hibernate.mapping.Join;

import java.time.LocalDate;

@Table(name = "reservations")
@NamedEntityGraph(
        name = "ReservationWithRelations",
        attributeNodes = {
                @NamedAttributeNode("room"),
                @NamedAttributeNode("user")
        }
)
@Entity
public class ReservationEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
    @ManyToOne
    @JoinColumn(name = "room_id")
    private RoomEntity room;
    @Column(name = "start_date")
    private LocalDate startDate;
    @Column(name = "end_date")
    private LocalDate endDate;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ReservationStatus status;


    public ReservationEntity() {
    }

    public ReservationEntity(Long id, LocalDate startDate, LocalDate endDate, ReservationStatus status) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public RoomEntity getRoom() {
        return room;
    }

    public void setRoom(RoomEntity room) {
        this.room = room;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }
}
