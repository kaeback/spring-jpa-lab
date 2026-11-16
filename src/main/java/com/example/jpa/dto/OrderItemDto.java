package com.example.jpa.dto;

import com.example.jpa.domain.OrderItem;

/**
 * 주문 상품 조회 결과 — 화면이나 다른 계층에 넘길 값만 담는다
 */
public record OrderItemDto(String productName, int orderPrice, int quantity) {

    public static OrderItemDto from(OrderItem orderItem) {
        return new OrderItemDto(
                orderItem.getProduct().getName(),
                orderItem.getOrderPrice(),
                orderItem.getQuantity());
    }
}
