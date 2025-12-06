package com.practice.reservation_system.users;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class UserService {
    private final UserRepository repository;
    private final UserMapper mapper;

    public UserService(UserRepository repository, UserMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }


    public List<User> findByFilter(
            UserSearchFilter filter
    ){
        var pageSize = filter.pageSize() != null ? filter.pageSize() : 10;
        var pageNum = filter.pageNum() != null ? filter.pageNum() : 0;

        var pageable = Pageable.ofSize(pageSize).withPage(pageNum);

        var allEntities = repository.findAllByFilter(
                filter.userId(),
                filter.username(),
                filter.role(),
                filter.userStatus(),
                pageable
        );

        return allEntities
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    public User findById(Long id) {
        UserEntity userEntity = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Not found reservation with id =" + id));


        return mapper.toDomain(userEntity);
    }

    public User createUser(User userToCreate) {
        var entityToCreate = mapper.toEntity(userToCreate);
        entityToCreate.setCreatedAt(LocalDate.now());
        entityToCreate.setUserStatus(UserStatus.ACTIVE);
        entityToCreate.setRole(RoleStatus.CLIENT);

        var savedEntity = repository.save(entityToCreate);

        return mapper.toDomain(savedEntity);
    }

    public User updateUser(Long id, User userToUpdate) {
        var entityToUpdate = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Not found user with id=" + id));

        entityToUpdate.setName(userToUpdate.username());
        entityToUpdate.setEmail(userToUpdate.email());
        entityToUpdate.setPhoneNumber(userToUpdate.phoneNumber());

        var updatedUser = repository.save(entityToUpdate);

        return mapper.toDomain(updatedUser);
    }

    public void blockUser(Long id) {
        var userToBlock = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Not found user with id=" + id));

        userToBlock.setUserStatus(UserStatus.BLOCKED);
        repository.save(userToBlock);
    }

    public void unblockUser(Long id) {
        var userToUnblock = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Not found user with id=" + id));

        userToUnblock.setUserStatus(UserStatus.ACTIVE);
        repository.save(userToUnblock);
    }


    public User setRole(Long id, RoleStatus status) {
        var userSetRole = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Not found user with id=" + id));

        userSetRole.setRole(status);
        var userToSave = repository.save(userSetRole);

        return mapper.toDomain(userToSave);
    }
}