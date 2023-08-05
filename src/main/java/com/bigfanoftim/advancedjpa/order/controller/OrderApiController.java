package com.bigfanoftim.advancedjpa.order.controller;

import com.bigfanoftim.advancedjpa.common.dto.Result;
import com.bigfanoftim.advancedjpa.order.domain.OrderRepository;
import com.bigfanoftim.advancedjpa.order.domain.query.OrderQueryDto;
import com.bigfanoftim.advancedjpa.order.domain.query.OrderQueryRepository;
import com.bigfanoftim.advancedjpa.order.dto.OrderDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
public class OrderApiController {

    private final OrderRepository orderRepository;
    private final OrderQueryRepository orderQueryRepository;

    @GetMapping("/api/v1/orders")
    public Result ordersWithItemsV1() {
        List<OrderDto> collect = orderRepository.findAllWithItems().stream()
                .map(order -> OrderDto.builder()
                        .order(order)
                        .build())
                .collect(Collectors.toList());

        return Result.builder()
                .count(collect.size())
                .data(collect)
                .build();
    }

    @GetMapping("/api/v2/orders")
    public Result ordersWithItemsV2(
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "limit", defaultValue = "100") int limit
    ) {
        List<OrderDto> collect = orderRepository.findAll(offset, limit).stream()
                .map(order -> OrderDto.builder()
                        .order(order)
                        .build())
                .collect(Collectors.toList());

        return Result.builder()
                .count(collect.size())
                .data(collect)
                .build();
    }

    @GetMapping("/api/v3/orders")
    public List<OrderQueryDto> ordersWithItemsV3(
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "limit", defaultValue = "100") int limit
    ) {
        return orderQueryRepository.findOrderQueryDtos();
    }
}
