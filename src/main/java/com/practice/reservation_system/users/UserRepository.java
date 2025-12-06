package com.practice.reservation_system.users;


import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    @Query("select u from UserEntity u where (:id is null or u.id = :id) and (:name is null or u.name = :name) and (:role is null or u.role = :role) and (:userStatus is null or u.userStatus = :userStatus)")
    List<UserEntity> findAllByFilter(
            @Param("id") Long userId,
            @Param("name") String username,
            @Param("role") RoleStatus role,
            @Param("userStatus") UserStatus userStatus,
            Pageable pageable
    );
}
