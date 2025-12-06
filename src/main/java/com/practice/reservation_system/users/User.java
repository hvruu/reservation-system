package com.practice.reservation_system.users;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.UniqueElements;

import java.time.LocalDate;

public record User(
        @Null
        Long id,
        @NotNull
        String email,
        @NotNull
        String username,
        @Pattern(
                regexp = "^(\\+7|8)7\\d{9}$",
                message = "Phone number must be a valid Kazakhstan number"
        )
        @NotNull
        String phoneNumber,
        RoleStatus roleStatus,
        @Null
        LocalDate createdAt,
        UserStatus userStatus
) {
}
