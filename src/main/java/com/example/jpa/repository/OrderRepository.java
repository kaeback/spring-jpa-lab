package com.example.jpa.repository;

import com.example.jpa.domain.Order;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    /** 전체 주문 — 주문만 조회한다. 회원·주문 상품은 사용할 때 조회한다 (N+1 문제) */
    public List<Order> findAll() {
        return em.createQuery("select o from Order o order by o.id", Order.class)
                .getResultList();
    }

    /** 전체 주문 — fetch join으로 회원, 주문 상품, 상품까지 SQL 한 번에 조회한다 */
    public List<Order> findAllWithItems() {
        return em.createQuery(
                        "select o from Order o"
                                + " join fetch o.member"
                                + " join fetch o.orderItems oi"
                                + " join fetch oi.product"
                                + " order by o.id", Order.class)
                .getResultList();
    }

    /** 주문 수 */
    public long count() {
        return em.createQuery("select count(o) from Order o", Long.class)
                .getSingleResult();
    }
}
