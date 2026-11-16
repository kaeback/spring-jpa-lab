package com.example.jpa.dto;

import com.example.jpa.domain.OrderStatus;

/**
 * 주문 요약 — JPQL의 select new로 필요한 컬럼만 조회해 바로 만든다
 */
public record OrderSummary(Long orderId, String memberName, OrderStatus status, Long totalPrice) {
}
