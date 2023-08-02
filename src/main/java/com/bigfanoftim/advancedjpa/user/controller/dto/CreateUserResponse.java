package com.bigfanoftim.advancedjpa.user.controller.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CreateUserResponse {

    private Long id;

    @Builder
    public CreateUserResponse(Long id) {
        this.id = id;
    }
}
