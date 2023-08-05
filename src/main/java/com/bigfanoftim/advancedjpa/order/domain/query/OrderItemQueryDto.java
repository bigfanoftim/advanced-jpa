package com.bigfanoftim.advancedjpa.order.domain.query;

import lombok.Getter;

@Getter
public class OrderItemQueryDto {

    private Long orderId;
    private Long itemId;
    private String itemName;
    private int itemPrice;

    public OrderItemQueryDto(Long orderId, Long itemId, String itemName, int itemPrice) {
        this.orderId = orderId;
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
    }
}
