package com.example.jpa.repository;

import com.example.jpa.domain.Order;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * 주문 리포지토리
 */
@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;

    public void save(Order order) {
        em.persist(order);
    }

    public Order findById(Long id) {
        return em.find(Order.class, id);
    }

    /** 주문 수 */
    public long count() {
        return em.createQuery("select count(o) from Order o", Long.class)
                .getSingleResult();
    }
}
