package com.bigfanoftim.advancedjpa.user.controller.dto;

import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateUserRequest {

    @NotNull
    private String name;

    @Builder
    public UpdateUserRequest(String name) {
        this.name = name;
    }
}
