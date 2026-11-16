package com.example.jpa.demo;

import com.example.jpa.domain.Order;
import com.example.jpa.domain.OrderItem;
import com.example.jpa.service.MemberService;
import com.example.jpa.service.OrderLine;
import com.example.jpa.service.OrderService;
import com.example.jpa.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * step6: N+1 문제와 fetch join
 */
@Component
@RequiredArgsConstructor
public class Step6Demo {

    private final MemberService memberService;
    private final ProductService productService;
    private final OrderService orderService;

    /** 실습 데이터 — 회원 3명이 한 번씩 주문한다 */
    public void prepare() {
        Demo.title("실습 데이터 준비");
        memberService.join("김철수", "kim@test.com");
        memberService.join("이영희", "lee@test.com");
        memberService.join("박민수", "park@test.com");
        productService.register("키보드", 50000, 10);
        productService.register("마우스", 20000, 20);
        productService.register("모니터", 300000, 5);
        orderService.order(1L, List.of(new OrderLine(1L, 1), new OrderLine(2L, 1)));
        orderService.order(2L, List.of(new OrderLine(3L, 1), new OrderLine(2L, 2)));
        orderService.order(3L, List.of(new OrderLine(1L, 2), new OrderLine(3L, 1)));
    }

    /** [6-1] N+1 — 주문 목록 1번 + 주문마다 회원·주문 상품 조회 */
    @Transactional(readOnly = true)
    public void nPlusOne() {
        Demo.title("[6-1] N+1 문제 — 주문 목록 출력");
        print(orderService.findOrders());
    }

    /** [6-2] fetch join — 필요한 엔티티를 SQL 한 번에 조회한다 */
    @Transactional(readOnly = true)
    public void fetchJoin() {
        Demo.title("[6-2] fetch join — 같은 목록을 SQL 한 번으로");
        print(orderService.findOrdersWithItems());
    }

    private void print(List<Order> orders) {
        for (Order order : orders) {
            StringBuilder items = new StringBuilder();
            for (OrderItem orderItem : order.getOrderItems()) {
                items.append(" ").append(orderItem.getProduct().getName())
                        .append(" ").append(orderItem.getQuantity()).append("개");
            }
            System.out.println("주문 " + order.getId() + ": " + order.getMember().getName() + " —" + items);
        }
    }
}
