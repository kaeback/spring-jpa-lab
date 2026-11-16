package com.example.jpa.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 주문 엔티티 — 회원 한 명이 여러 번 주문한다 (주문 N : 회원 1)
 */
@Entity
@Table(name = "orders")   // order는 SQL 예약어(ORDER BY)라 테이블 이름으로 쓸 수 없다
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)                    // 주문 N : 회원 1, 회원은 필요할 때 조회한다
    @JoinColumn(name = "member_id", nullable = false)     // 외래 키 컬럼 이름
    private Member member;

    // 주문 1 : 주문 상품 N — 외래 키는 OrderItem.order가 관리한다(연관관계의 주인)
    // cascade: 주문을 저장·삭제하면 주문 상품도 함께, orphanRemoval: 목록에서 빠진 주문 상품은 DELETE
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    @Column(nullable = false)
    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private OrderStatus status;

    public Order(Member member) {
        this.member = member;
        this.orderDate = LocalDateTime.now();
        this.status = OrderStatus.ORDERED;
    }

    /** 연관관계 편의 메서드 — 양쪽 객체에 함께 값을 넣는다 */
    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);   // 주인 쪽에 넣어야 외래 키(order_id)가 저장된다
    }

    /** 주문 상품 하나 빼기 — 재고를 되돌리고 목록에서 뺀다 (orphanRemoval로 DELETE) */
    public void removeOrderItem(OrderItem orderItem) {
        orderItem.cancel();
        orderItems.remove(orderItem);
    }

    /** 주문 취소 — 상태를 바꾸고 재고를 되돌린다 */
    public void cancel() {
        if (status == OrderStatus.CANCELED) {
            throw new IllegalStateException("이미 취소된 주문입니다. id=" + id);
        }
        this.status = OrderStatus.CANCELED;
        for (OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }
    }

    /** 주문 총액 */
    public int getTotalPrice() {
        int total = 0;
        for (OrderItem orderItem : orderItems) {
            total += orderItem.getTotalPrice();
        }
        return total;
    }
}
