package com.practice.reservation_system.rooms;

import com.practice.reservation_system.reservations.ReservationEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Null;

import java.util.List;

@Entity
@Table(name="rooms")
public class RoomEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;
    @Column(name="name")
    private String name;
    @Column(name="capacity")
    private Integer capacity;
    @Enumerated(EnumType.STRING)
    @Column(name="type")
    private RoomType type;
    @Column(name="price")
    private Long price;
    @Enumerated(EnumType.STRING)
    @Column(name="status")
    private RoomStatus status;

    @OneToMany(mappedBy = "room")
    private List<ReservationEntity> reservation;

    public RoomEntity() {
    }

    public RoomEntity(Long id, String name, Integer capacity, RoomType type, Long price, RoomStatus status) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
        this.type = type;
        this.price = price;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public RoomType getType() {
        return type;
    }

    public void setType(RoomType type) {
        this.type = type;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public RoomStatus getStatus() {
        return status;
    }

    public void setStatus(RoomStatus status) {
        this.status = status;
    }

    public List<ReservationEntity> getReservation() {
        return reservation;
    }

    public void setReservation(List<ReservationEntity> reservation) {
        this.reservation = reservation;
    }
}
