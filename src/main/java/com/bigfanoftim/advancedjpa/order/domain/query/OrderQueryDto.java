package com.bigfanoftim.advancedjpa.order.domain.query;

import com.bigfanoftim.advancedjpa.user.domain.User;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class OrderQueryDto {

    private Long orderId;
    private String userName;
    private List<OrderItemQueryDto> orderItems;

    public OrderQueryDto(Long orderId, String userName) {
        this.orderId = orderId;
        this.userName = userName;
    }

    public void setOrderItems(List<OrderItemQueryDto> orderItems) {
        this.orderItems = orderItems;
    }
}
