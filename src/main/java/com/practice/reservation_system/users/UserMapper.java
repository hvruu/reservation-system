package com.practice.reservation_system.users;

public class UserMapper {

    public User toDomain(
            UserEntity entity
    ){
        return new User(
                entity.getId(),
                entity.getEmail(),
                entity.getName(),
                entity.getPhoneNumber(),
                entity.getRole(),
                entity.getCreatedAt(),
                entity.getUserStatus()
        );
    }

    public UserEntity toEntity(
            User user
    ){
        return new UserEntity(
                user.id(),
                user.username(),
                user.email(),
                user.phoneNumber(),
                user.roleStatus(),
                user.createdAt(),
                user.userStatus()
        );
    }
}
