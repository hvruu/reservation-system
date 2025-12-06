package com.practice.reservation_system.users;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService service;
    private static final Logger log =
            LoggerFactory.getLogger(UserController.class);


    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(
            @RequestParam(name="userId", required = false) Long userId,
            @RequestParam(name="username", required = false) String username,
            @RequestParam(name="role", required = false) RoleStatus role,
            @RequestParam(name="userStatus", required = false) UserStatus userStatus,
            @RequestParam(name="pageSize", required = false) Integer pageSize,
            @RequestParam(name="pageNum", required = false) Integer pageNum
    ){
        log.info("called method getAllUsers");

        var filter = new UserSearchFilter(
                userId,
                username,
                role,
                userStatus,
                pageSize,
                pageNum
        );

        return ResponseEntity.ok(service.findByFilter(filter));
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(
            @PathVariable("id") Long id
    ){
        log.info("called method getUserById");

        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<User> createUser(
            @RequestBody @Valid User userToCreate
    ){
        log.info("called method createUser");

        return ResponseEntity.ok(service.createUser(userToCreate));
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(
            @PathVariable("id") Long id,
            @RequestBody @Valid User userToUpdate
    ){
        log.info("called method updateUser");

        return ResponseEntity.ok(service.updateUser(id, userToUpdate));
    }

    @DeleteMapping("/block/{id}")
    public ResponseEntity<Void> blockUser(
            @PathVariable("id") Long id
    ){
        log.info("called method calcenUser");

        service.blockUser(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/unblock/{id}")
    public ResponseEntity<Void> unblockUser(
            @PathVariable("id") Long id
    ){
        log.info("called method ublockUser");

        service.unblockUser(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/role/{id}")
    public ResponseEntity<User> setRole(
            @PathVariable Long id,
            @RequestBody RoleStatus status
    ){
        log.info("called method setRole");

        return ResponseEntity.ok(service.setRole(id, status));
    }
}