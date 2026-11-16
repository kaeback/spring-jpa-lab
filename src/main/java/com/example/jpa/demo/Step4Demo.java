package com.example.jpa.demo;

import com.example.jpa.domain.OrderItem;
import com.example.jpa.domain.Product;
import com.example.jpa.repository.OrderItemRepository;
import com.example.jpa.service.MemberService;
import com.example.jpa.service.OrderService;
import com.example.jpa.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * step4: @ManyToOne 단방향 연관관계, 지연 로딩, 예외와 롤백
 */
@Component
@RequiredArgsConstructor
public class Step4Demo {

    private final MemberService memberService;
    private final ProductService productService;
    private final OrderService orderService;
    private final OrderItemRepository orderItemRepository;

    /** 실습 데이터 — 회원 1명, 상품 2개 */
    public void prepare() {
        Demo.title("실습 데이터 준비");
        memberService.join("김철수", "kim@test.com");
        productService.register("키보드", 50000, 10);
        productService.register("마우스", 20000, 20);
    }

    /** [4-1] 주문 — 주문과 주문 상품을 저장하고 상품 재고를 줄인다. 주문 id를 돌려준다 */
    public Long order() {
        Demo.title("[4-1] 주문하기 — 김철수가 키보드 2개");
        Long orderId = orderService.order(1L, 1L, 2);
        System.out.println("주문 완료: 주문 id=" + orderId);
        System.out.println("주문 후 상품: " + productService.findProduct(1L));
        return orderId;
    }

    /** [4-2] 지연 로딩 — 연관된 엔티티는 실제로 사용할 때 조회한다 */
    @Transactional(readOnly = true)
    public void lazyLoading(Long orderId) {
        Demo.title("[4-2] 지연 로딩과 프록시");
        OrderItem orderItem = orderItemRepository.findByOrderId(orderId).get(0);
        Product product = orderItem.getProduct();
        System.out.println("product 클래스: " + product.getClass().getSimpleName());
        System.out.println("초기화되었나? " + Hibernate.isInitialized(product));

        System.out.println("상품 이름 사용");
        System.out.println("상품 이름: " + product.getName());   // 이때 SELECT
        System.out.println("초기화되었나? " + Hibernate.isInitialized(product));
        System.out.println("주문 금액: " + orderItem.getTotalPrice());
    }

    /** [4-3] 재고 부족 — 예외가 발생하면 트랜잭션 전체가 롤백된다 */
    public void outOfStock() {
        Demo.title("[4-3] 재고 부족과 롤백 — 마우스 100개 주문");
        System.out.println("주문 전 주문 수: " + orderService.countOrders());
        try {
            orderService.order(1L, 2L, 100);
        } catch (IllegalStateException e) {
            System.out.println("예외: " + e.getMessage());
        }
        System.out.println("주문 후 주문 수: " + orderService.countOrders());
        System.out.println("마우스: " + productService.findProduct(2L));
    }
}
