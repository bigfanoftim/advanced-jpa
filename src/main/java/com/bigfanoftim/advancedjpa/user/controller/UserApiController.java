package com.bigfanoftim.advancedjpa.user.controller;

import com.bigfanoftim.advancedjpa.common.dto.Result;
import com.bigfanoftim.advancedjpa.user.controller.dto.*;
import com.bigfanoftim.advancedjpa.user.domain.User;
import com.bigfanoftim.advancedjpa.user.domain.UserRepository;
import com.bigfanoftim.advancedjpa.user.service.UserService;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
public class UserApiController {

    private final UserService userService;
    private final UserRepository userRepository;

    @PostMapping("/api/v1/users")
    public CreateUserResponse saveUserV1(@RequestBody @Valid CreateUserRequest createUserRequest) {
        Long id = userService.save(createUserRequest);

        return CreateUserResponse.builder().id(id).build();
    }

    @PutMapping("/api/v1/users/{id}")
    public UpdateUserResponse updateUserV1(
            @PathVariable Long id,
            @RequestBody @Valid UpdateUserRequest updateUserRequest
    ) {
        userService.update(id, updateUserRequest.getName());

        /**
         * osiv default value는 true이다.
         * 따라서 Persistence Context가 계속 유지되어 findOne 조회 시 SELECT SQL이 사용되지 않고
         * 1차 캐시에서 해당 Entity를 그대로 활용한다.
         *
         * application.yml에 spring.jpa.open-in-view: false 설정을 추가하자.
         *
         * Command, Query를 분리하는 CQRS도 참고해보자.
         * www.youtube.com/watch?v=BnS6343GTkY - 배달의 민족 마이크로서비스 여행기 (영한님)
         */
        User user = userService.findOne(id);

        return UpdateUserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }

    @GetMapping("/api/v1/users")
    public Result getUsers() {
        List<UserDto> collect = userService.findAll().stream()
                .map(user -> UserDto.builder()
                        .name(user.getName())
                        .build())
                .collect(Collectors.toList());

        return Result.builder()
                .count(collect.size())
                .data(collect)
                .build();
    }

    /**
     * http://localhost:8080/api/v2/users?page=0&size=5&sort=id,desc
     *
     * application.yml에 default-page-size 추가 가능
     */
    @GetMapping("/api/v2/users")
    public Page<UserDto> getPageUsers(@PageableDefault(size = 5, sort = "id") Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(UserDto::new);
    }

//    @PostConstruct
    public void init() {
        for (int i = 1; i <= 100; i++) {
            User user = new User("user" + i, i);
            userRepository.save(user);
        }
    }
}
