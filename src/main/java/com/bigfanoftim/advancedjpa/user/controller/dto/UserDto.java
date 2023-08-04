package com.bigfanoftim.advancedjpa.user.controller.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserDto {

    private String name;

    @Builder
    public UserDto(String name) {
        this.name = name;
    }
}
