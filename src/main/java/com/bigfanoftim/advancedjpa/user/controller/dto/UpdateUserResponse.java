package com.bigfanoftim.advancedjpa.user.controller.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UpdateUserResponse {

    private Long id;
    private String name;

    @Builder
    public UpdateUserResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
