package com.example.jpa.demo;

import com.example.jpa.domain.Member;
import com.example.jpa.domain.Order;
import com.example.jpa.domain.OrderItem;
import com.example.jpa.domain.Product;
import com.example.jpa.service.MemberService;
import com.example.jpa.service.OrderLine;
import com.example.jpa.service.OrderService;
import com.example.jpa.service.ProductService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * step5: 양방향 연관관계와 연관관계의 주인, cascade, orphanRemoval
 */
@Component
@RequiredArgsConstructor
public class Step5Demo {

    private final MemberService memberService;
    private final ProductService productService;
    private final OrderService orderService;
    private final EntityManager em;

    /** 실습 데이터 — 회원 1명, 상품 3개 */
    public void prepare() {
        Demo.title("실습 데이터 준비");
        memberService.join("김철수", "kim@test.com");
        productService.register("키보드", 50000, 10);
        productService.register("마우스", 20000, 20);
        productService.register("모니터", 300000, 5);
    }

    /** [5-1] 주인이 아닌 쪽(Order.orderItems)에만 넣으면 외래 키가 비어 있다 — 예외가 발생한다 */
    @Transactional
    public void inverseSideOnly() {
        Demo.title("[5-1] 주인이 아닌 쪽에만 값을 넣으면");
        Member member = em.find(Member.class, 1L);
        Product keyboard = em.find(Product.class, 1L);

        Order order = new Order(member);
        OrderItem orderItem = new OrderItem(keyboard, 1);
        order.getOrderItems().add(orderItem);   // orderItem.order는 null 그대로
        System.out.println("orderItem.getOrder() = " + orderItem.getOrder());
        em.persist(order);                      // cascade로 orderItem도 저장하려고 한다
        em.flush();
    }

    /** [5-2] cascade — 주문만 저장해도 주문 상품이 함께 저장된다. 주문 id를 돌려준다 */
    public Long cascade() {
        Demo.title("[5-2] cascade — 키보드 1개, 마우스 2개 주문");
        Long orderId = orderService.order(1L, List.of(
                new OrderLine(1L, 1),
                new OrderLine(2L, 2)));
        System.out.println("주문 완료: 주문 id=" + orderId);
        return orderId;
    }

    /** [5-3] 양방향 조회 — 주문에서 주문 상품 목록으로 이동한다 */
    @Transactional(readOnly = true)
    public void readOrder(Long orderId) {
        Demo.title("[5-3] 주문 조회 — Order에서 orderItems로");
        Order order = orderService.findOrder(orderId);
        System.out.println("주문자: " + order.getMember().getName());
        for (OrderItem orderItem : order.getOrderItems()) {
            System.out.println("  " + orderItem.getProduct().getName() + " " + orderItem.getQuantity()
                    + "개 = " + orderItem.getTotalPrice() + "원");
        }
        System.out.println("총액: " + order.getTotalPrice() + "원");
    }

    /** [5-4] 주문 취소 — 상태와 재고가 변경 감지로 UPDATE된다 */
    public void cancel(Long orderId) {
        Demo.title("[5-4] 주문 취소");
        orderService.cancel(orderId);
        System.out.println("상태: " + orderService.findOrder(orderId).getStatus());
        System.out.println(productService.findProduct(1L));
        System.out.println(productService.findProduct(2L));
    }

    /** [5-5] orphanRemoval — 목록에서 뺀 주문 상품은 DELETE된다 */
    public void orphanRemoval() {
        Demo.title("[5-5] orphanRemoval — 모니터 1개, 마우스 1개 주문 후 마우스 빼기");
        Long orderId = orderService.order(1L, List.of(
                new OrderLine(3L, 1),
                new OrderLine(2L, 1)));
        System.out.println("주문 완료: 주문 id=" + orderId);
        System.out.println("마우스 빼기");
        orderService.removeOrderItem(orderId, 2L);
        System.out.println(productService.findProduct(2L));
    }
}
