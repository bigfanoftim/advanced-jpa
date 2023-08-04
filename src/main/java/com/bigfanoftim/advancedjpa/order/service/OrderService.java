package com.bigfanoftim.advancedjpa.order.service;

import com.bigfanoftim.advancedjpa.order.domain.Order;
import com.bigfanoftim.advancedjpa.order.domain.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public List<Order> findAllWithItems() {
        return orderRepository.findAllWithItems();
    }
}
