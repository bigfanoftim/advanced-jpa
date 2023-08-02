package com.bigfanoftim.advancedjpa.user.controller;

import com.bigfanoftim.advancedjpa.user.controller.dto.CreateUserRequest;
import com.bigfanoftim.advancedjpa.user.controller.dto.CreateUserResponse;
import com.bigfanoftim.advancedjpa.user.controller.dto.UpdateUserRequest;
import com.bigfanoftim.advancedjpa.user.controller.dto.UpdateUserResponse;
import com.bigfanoftim.advancedjpa.user.domain.User;
import com.bigfanoftim.advancedjpa.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class UserApiController {

    private final UserService userService;

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
}
