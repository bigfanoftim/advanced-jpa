package com.bigfanoftim.advancedjpa.order.dto;

import com.bigfanoftim.advancedjpa.order.domain.Order;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class OrderDto {

    private Long orderId;
    private String nameOfUser;
    private List<OrderItemDto> orderItems;

    @Builder
    public OrderDto(Order order) {
        this.orderId = order.getId();
        this.nameOfUser = order.getUser().getName();
        this.orderItems = order.getOrderItems().stream()
                .map(item -> OrderItemDto.builder()
                        .orderItem(item)
                        .build())
                .collect(Collectors.toList());
    }
}
