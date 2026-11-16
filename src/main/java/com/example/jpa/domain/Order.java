package com.example.jpa.domain;

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
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
}
