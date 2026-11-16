package com.example.jpa.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 상품 엔티티 — PK를 시퀀스(product_seq)로 만든다
 */
@Entity
@SequenceGenerator(
        name = "product_seq_generator",   // 이 생성기의 이름 (아래 @GeneratedValue에서 쓴다)
        sequenceName = "product_seq",     // DB에 만들 시퀀스 이름
        allocationSize = 1)               // 시퀀스를 한 번에 1씩 가져온다
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_seq_generator")
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    private int price;           // 기본형(int)은 null이 될 수 없어 NOT NULL 컬럼이 된다

    private int stockQuantity;   // 재고 수량 — 컬럼 이름은 stock_quantity

    public Product(String name, int price, int stockQuantity) {
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    /** 가격을 바꾼다 */
    public void changePrice(int price) {
        this.price = price;
    }

    /** 재고를 줄인다 — 재고보다 많이 주문하면 예외 */
    public void removeStock(int quantity) {
        if (stockQuantity < quantity) {
            throw new IllegalStateException(name + " 재고가 부족합니다. (재고 " + stockQuantity + "개, 주문 " + quantity + "개)");
        }
        this.stockQuantity -= quantity;
    }

    /** 재고를 늘린다 (주문 취소) */
    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }

    @Override
    public String toString() {
        return "Product{id=" + id + ", name=" + name + ", price=" + price + ", stock=" + stockQuantity + "}";
    }
}
