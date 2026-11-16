package com.example.jpa.service;

/**
 * 주문할 상품 한 줄 — 상품 id와 수량
 */
public record OrderLine(Long productId, int quantity) {
}
