package com.example.jpa.demo;

import com.example.jpa.domain.Order;
import com.example.jpa.dto.OrderDto;
import com.example.jpa.dto.OrderItemDto;
import com.example.jpa.dto.OrderSummary;
import com.example.jpa.service.MemberService;
import com.example.jpa.service.OrderLine;
import com.example.jpa.service.OrderService;
import com.example.jpa.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * step8: 트랜잭션 밖의 지연 로딩 예외와 DTO
 * 이 클래스의 메서드에는 @Transactional이 없다 — 서비스가 돌려준 결과를 트랜잭션 밖에서 사용한다.
 */
@Component
@RequiredArgsConstructor
public class Step8Demo {

    private final MemberService memberService;
    private final ProductService productService;
    private final OrderService orderService;

    /** 실습 데이터 — 주문 2건 */
    public void prepare() {
        Demo.title("실습 데이터 준비");
        memberService.join("김철수", "kim@test.com");
        memberService.join("이영희", "lee@test.com");
        productService.register("키보드", 50000, 10);
        productService.register("마우스", 20000, 20);
        productService.register("모니터", 300000, 5);
        orderService.order(1L, List.of(new OrderLine(1L, 1), new OrderLine(2L, 2)));
        orderService.order(2L, List.of(new OrderLine(3L, 1)));
    }

    /** [8-1] 엔티티를 트랜잭션 밖에서 사용하면 — 지연 로딩할 수 없다 */
    public void lazyException() {
        Demo.title("[8-1] 트랜잭션 밖에서 지연 로딩");
        Order order = orderService.findOrder(1L);   // 서비스 메서드가 끝나면서 트랜잭션도 끝났다
        System.out.println("주문 id: " + order.getId() + ", 상태: " + order.getStatus());
        try {
            System.out.println("주문자: " + order.getMember().getName());
        } catch (RuntimeException e) {
            System.out.println("예외: " + e.getClass().getSimpleName());
            System.out.println("메시지: " + e.getMessage());
        }
    }

    /** [8-2] 트랜잭션 안에서 DTO로 바꿔 돌려준다 */
    public void dto() {
        Demo.title("[8-2] 서비스에서 DTO로 변환");
        OrderDto dto = orderService.findOrderDto(1L);
        System.out.println("주문 id: " + dto.orderId() + ", 주문자: " + dto.memberName()
                + ", 상태: " + dto.status() + ", 총액: " + dto.totalPrice() + "원");
        for (OrderItemDto item : dto.items()) {
            System.out.println("  " + item);
        }
    }

    /** [8-3] JPQL로 DTO를 바로 조회한다 — 필요한 컬럼만 SELECT */
    public void selectNew() {
        Demo.title("[8-3] select new로 DTO 조회");
        for (OrderSummary summary : orderService.findOrderSummaries()) {
            System.out.println(summary);
        }
    }
}
