package com.example.jpa.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 주문 상품 엔티티 — 주문 하나에 담긴 상품 한 종류와 수량
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)              // 주문 상품 N : 주문 1 — 연관관계의 주인
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)              // 주문 상품 N : 상품 1
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    private int orderPrice;   // 주문할 때의 가격 (상품 가격이 나중에 바뀌어도 그대로)

    private int quantity;

    /** 주문 상품을 만들면서 상품 재고를 줄인다. 주문은 Order.addOrderItem()이 연결한다 */
    public OrderItem(Product product, int quantity) {
        product.removeStock(quantity);
        this.product = product;
        this.orderPrice = product.getPrice();
        this.quantity = quantity;
    }

    /** Order.addOrderItem()에서만 호출한다 (같은 패키지에서만 보인다) */
    void setOrder(Order order) {
        this.order = order;
    }

    /** 주문 취소 — 재고를 되돌린다 */
    public void cancel() {
        product.addStock(quantity);
    }

    /** 주문 금액 = 주문 가격 × 수량 */
    public int getTotalPrice() {
        return orderPrice * quantity;
    }
}
