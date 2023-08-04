package com.bigfanoftim.advancedjpa.user.controller.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Result<T> {

    private int count;
    private T data;

    @Builder
    public Result(int count, T data) {
        this.count = count;
        this.data = data;
    }
}
