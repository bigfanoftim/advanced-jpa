package com.bigfanoftim.advancedjpa.user.controller.dto;

import com.bigfanoftim.advancedjpa.user.domain.User;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateUserRequest {

    @NotNull(message = "user name cannot be null")
    private String name;

    @Builder
    public CreateUserRequest(String name) {
        this.name = name;
    }

    public User toEntity() {
        return User.builder()
                .name(name)
                .build();
    }
}
