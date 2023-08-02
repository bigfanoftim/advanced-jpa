package com.bigfanoftim.advancedjpa.user.service;

import com.bigfanoftim.advancedjpa.user.domain.User;
import com.bigfanoftim.advancedjpa.user.domain.UserRepository;
import com.bigfanoftim.advancedjpa.user.controller.dto.CreateUserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public Long save(CreateUserRequest requestDto) {
        User user = userRepository.save(requestDto.toEntity());

        return user.getId();
    }

    @Transactional(readOnly = true)
    public User findOne(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("없는 user입니다. id=" + id));
    }

    @Transactional
    public void update(Long id, String name) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("없는 user입니다. id=" + id));
        user.updateName(name);
    }
}
