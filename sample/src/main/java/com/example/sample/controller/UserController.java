package com.example.sample.controller;

import com.example.sample.dto.UserNoteSummary;
import com.example.sample.dto.UserRequest;
import com.example.sample.dto.UserResponse;
import com.example.sample.exception.BusinessException;
import com.example.sample.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
// @Validated is in org.springframework.validation
//@Validated
public class UserController {

//    @RequiredArgsConstructor should create the constructor
    private final UserService userService;

    @PostMapping
    public UserResponse create(@Valid @RequestBody UserRequest request) {
        return userService.create(request);
    }

    @GetMapping
    public List<UserResponse> findAll() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public UserResponse findById(@Valid @PathVariable String id) {
        Long longId = Long.MIN_VALUE;
        try{
            longId = Long.parseLong(id);
        } catch (NumberFormatException ex) {
            throw new BusinessException("NumberFormatException: " + ex.getMessage());
        }
        return userService.findById(longId);
    }

    @PutMapping("/{id}")
//    @Valid is in jakarta.validation
//    public UserResponse update(@PathVariable Long id, @Valid @RequestBody UserRequest request) {
//    Pessimistic lock version
    public UserResponse updateUser(@PathVariable Long id, @Valid @RequestBody UserRequest request) {
        return userService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        userService.delete(id);
    }

    @GetMapping("/{id}/note/summary")
    public UserNoteSummary getNoteSummary(@PathVariable Long id) {
        return userService.getNoteSummary(id);
    }






}
