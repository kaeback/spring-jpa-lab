package com.example.jpa.repository;

import com.example.jpa.domain.OrderItem;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 주문 상품 리포지토리
 */
@Repository
@RequiredArgsConstructor
public class OrderItemRepository {

    private final EntityManager em;

    public void save(OrderItem orderItem) {
        em.persist(orderItem);
    }

    /** 주문 하나에 담긴 주문 상품 — JPQL에서 연관된 엔티티의 필드는 oi.order.id처럼 점(.)으로 따라간다 */
    public List<OrderItem> findByOrderId(Long orderId) {
        return em.createQuery("select oi from OrderItem oi where oi.order.id = :orderId order by oi.id", OrderItem.class)
                .setParameter("orderId", orderId)
                .getResultList();
    }
}
