package com.example.sample.service;

import java.util.List;

import com.example.sample.dto.UserNoteSummary;
import com.example.sample.dto.UserRequest;
import com.example.sample.dto.UserResponse;

public interface UserService {

    UserResponse create(UserRequest request);

    List<UserResponse> findAll();

    UserResponse findById(Long id);

    UserResponse update(Long id, UserRequest request);

    void delete(Long id);

    UserNoteSummary getNoteSummary(Long id);

}
