package com.example.jpa.dto;

import com.example.jpa.domain.Order;
import com.example.jpa.domain.OrderStatus;

import java.util.List;

/**
 * 주문 조회 결과 — 엔티티 대신 트랜잭션 밖으로 넘기는 객체
 */
public record OrderDto(Long orderId, String memberName, OrderStatus status, int totalPrice,
                       List<OrderItemDto> items) {

    /** 엔티티를 DTO로 바꾼다 — 지연 로딩이 일어나므로 트랜잭션 안에서 호출한다 */
    public static OrderDto from(Order order) {
        return new OrderDto(
                order.getId(),
                order.getMember().getName(),
                order.getStatus(),
                order.getTotalPrice(),
                order.getOrderItems().stream().map(OrderItemDto::from).toList());
    }
}
