package com.practice.reservation_system.users;

public record UserSearchFilter (
        Long userId,
        String username,
        RoleStatus role,
        UserStatus userStatus,
        Integer pageSize,
        Integer pageNum
){
}
