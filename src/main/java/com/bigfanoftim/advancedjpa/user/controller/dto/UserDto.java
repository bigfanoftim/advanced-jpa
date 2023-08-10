package com.bigfanoftim.advancedjpa.user.controller.dto;

import com.bigfanoftim.advancedjpa.user.domain.User;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserDto {

    private Long userId;
    private String name;
    private int age;

    @QueryProjection
    public UserDto(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Builder
    public UserDto(String name) {
        this.name = name;
    }

    public UserDto(User user) {
        this.userId = user.getId();
        this.name = user.getName();
        this.age = user.getAge();
    }
}
