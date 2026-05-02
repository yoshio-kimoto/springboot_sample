package com.example.sample.service;

import com.example.sample.dto.UserNoteSummary;
import com.example.sample.dto.UserRequest;
import com.example.sample.dto.UserResponse;
import com.example.sample.entity.UserEntity;
import com.example.sample.exception.ResourceNotFoundException;
import com.example.sample.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    //    @RequiredArgsConstructor should create the constructor
    private final UserRepository userRepository;

    @Override
    public UserResponse create(UserRequest request) {
        UserEntity entity = UserEntity.builder()
//                .name(request.getName())
//                .email(request.getEmail())
                .name(request.name())
                .email(request.email())
                .note(request.note())
                .build();
        UserEntity saved = userRepository.save(entity);
        return toResponse(saved);
    }

    @Override
    public List<UserResponse> findAll() {
        return userRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
//        return List.of();
    }

    @Override
    public UserResponse findById(Long id) {
        UserEntity entity = userRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("User not found: Requested ID: " + id));
                .orElseThrow(() -> new ResourceNotFoundException("User not found: Requested ID: " + id));
        return toResponse(entity);
    }

    @Transactional
    @Override
    public UserResponse update(Long id, UserRequest request) {
        UserEntity entity = userRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("User not found"));
                .orElseThrow(() -> new ResourceNotFoundException("User not found: Requested ID: " + id));
//        entity.setName(request.getName());
//        entity.setEmail(request.getEmail());

//        record version
        entity.setName(request.name());
        entity.setEmail(request.email());

//        @Transactional takes care of this.
//        userRepository.save(entity);

        return toResponse(entity);
    }

//    Pessimistic lock
    @Transactional
    public UserResponse updateUser(Long id, UserRequest request) {
        UserEntity entity = userRepository.findByIdForUpdate(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
//        entity.setName(request.getName());
//        entity.setEmail(request.getEmail());

//        record version
        entity.setName(request.name());
        entity.setEmail(request.email());

        return toResponse(entity);
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    public UserNoteSummary getNoteSummary(Long id) {
        UserEntity entity = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + id));
        String note = entity.getNote();
        String summary = (note == null) ? "" : note.substring(0, Math.min(10, note.length()));
        return new UserNoteSummary(summary);
    }

    private UserResponse toResponse(UserEntity entity) {
//        return UserResponse.builder()
//                .id(entity.getId())
//                .name(entity.getName())
//                .email(entity.getEmail())
//                .build();

        // record version
        return new UserResponse(entity.getId(), entity.getName(), entity.getEmail(), entity.getNote());
    }


}
