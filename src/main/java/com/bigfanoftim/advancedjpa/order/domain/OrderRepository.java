package com.bigfanoftim.advancedjpa.order.domain;

import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class OrderRepository {

    private final EntityManager em;

    @Transactional(readOnly = true)
    public List<Order> findAll(int offset, int limit) {
        return em.createQuery("select o from Order o" +
                        " join fetch o.user u", Order.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    @Transactional(readOnly = true)
    public List<Order> findAllWithItems() {
        return em.createQuery("select distinct o from Order o" +
                        " join fetch o.user u" +
                        " join fetch o.orderItems oi" +
                        " join fetch oi.item i", Order.class)
                .getResultList();
    }
}
