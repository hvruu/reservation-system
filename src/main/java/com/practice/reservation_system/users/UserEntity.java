package com.practice.reservation_system.users;

import com.practice.reservation_system.reservations.ReservationEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.UniqueElements;

import java.time.LocalDate;
import java.util.List;

@Table(name = "users")
@Entity
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @NotNull
    @Column(name = "username", unique = true)
    private String name;
    @NotNull
    @Column(name = "email", unique = true)
    private String email;
    @NotNull
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "role")
    @Enumerated
    private RoleStatus role;
    @Column(name = "created_at")
    @FutureOrPresent
    private LocalDate createdAt;
    @Column(name = "user_status")
    @Enumerated
    private UserStatus userStatus;

    @OneToMany(mappedBy = "user")
    private List<ReservationEntity> reservation;


    public UserEntity() {
    }

    public UserEntity(Long id, String name, String email, String phoneNumber, RoleStatus role, LocalDate createdAt, UserStatus userStatus) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.createdAt = createdAt;
        this.userStatus = userStatus;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setRole(RoleStatus role) {
        this.role = role;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public void setUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public RoleStatus getRole() {
        return role;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public UserStatus getUserStatus() {
        return userStatus;
    }
}
