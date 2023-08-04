package com.bigfanoftim.advancedjpa.order.dto;

import com.bigfanoftim.advancedjpa.order.domain.OrderItem;
import lombok.Builder;
import lombok.Getter;

@Getter
public class OrderItemDto {

    private Long itemId;
    private String itemName;
    private int itemPrice;

    @Builder
    public OrderItemDto(OrderItem orderItem) {
        this.itemId = orderItem.getItem().getId();
        this.itemName = orderItem.getItem().getName();
        this.itemPrice = orderItem.getItem().getPrice();
    }
}
