package com.bigfanoftim.advancedjpa.user.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class UserQueryDslDto {

    private String username;
    private int age;

    public UserQueryDslDto(String username, int age) {
        this.username = username;
        this.age = age;
    }
}
