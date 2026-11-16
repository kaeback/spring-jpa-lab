package com.example.jpa.demo;

import com.example.jpa.domain.Grade;
import com.example.jpa.domain.Order;
import com.example.jpa.domain.OrderStatus;
import com.example.jpa.repository.MemberRepository;
import com.example.jpa.repository.OrderRepository;
import com.example.jpa.repository.ProductRepository;
import com.example.jpa.service.MemberService;
import com.example.jpa.service.OrderLine;
import com.example.jpa.service.OrderService;
import com.example.jpa.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * step7: Spring Data JPA — JpaRepository, 쿼리 메서드, @Query
 */
@Component
@RequiredArgsConstructor
public class Step7Demo {

    private final MemberService memberService;
    private final ProductService productService;
    private final OrderService orderService;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    /** 실습 데이터 — 서비스 코드는 step6과 같고, 리포지토리만 바뀌었다 */
    public void prepare() {
        Demo.title("실습 데이터 준비");
        memberService.join("김철수", "kim@test.com");
        memberService.join("이영희", "lee@test.com");
        memberService.join("박민수", "park@test.com");
        memberService.changeGrade(2L, Grade.VIP);
        productService.register("키보드", 50000, 10);
        productService.register("마우스", 20000, 20);
        productService.register("모니터", 300000, 5);
        productService.register("무선 키보드", 70000, 7);
        orderService.order(1L, List.of(new OrderLine(1L, 1)));
        orderService.order(2L, List.of(new OrderLine(3L, 1), new OrderLine(2L, 1)));
        orderService.order(1L, List.of(new OrderLine(4L, 1)));
        orderService.cancel(1L);
    }

    /** [7-1] JpaRepository가 기본으로 주는 메서드 */
    public void basicMethods() {
        Demo.title("[7-1] JpaRepository 기본 메서드");
        System.out.println("count(): " + memberRepository.count());
        System.out.println("findById(1): " + memberRepository.findById(1L));
        System.out.println("findById(99): " + memberRepository.findById(99L));
        System.out.println("existsById(2): " + memberRepository.existsById(2L));
    }

    /** [7-2] 쿼리 메서드 — 메서드 이름으로 만든 JPQL */
    public void queryMethods() {
        Demo.title("[7-2] 쿼리 메서드");
        System.out.println("findByEmail: " + memberRepository.findByEmail("lee@test.com"));
        System.out.println("findByGradeOrderByIdAsc(VIP): " + memberRepository.findByGradeOrderByIdAsc(Grade.VIP));
        System.out.println("findByNameContainingOrderByIdAsc(키보드):");
        productRepository.findByNameContainingOrderByIdAsc("키보드").forEach(p -> System.out.println("  " + p));
        System.out.println("findByPriceBetweenOrderByPriceAsc(30000, 100000):");
        productRepository.findByPriceBetweenOrderByPriceAsc(30000, 100000).forEach(p -> System.out.println("  " + p));
        System.out.println("findByMemberIdOrderByIdAsc(1): 주문 "
                + orderRepository.findByMemberIdOrderByIdAsc(1L).size() + "건");
    }

    /** [7-3] @Query — JPQL을 직접 쓴다 */
    @Transactional(readOnly = true)
    public void query() {
        Demo.title("[7-3] @Query");
        System.out.println("findAllWithItems():");
        for (Order order : orderRepository.findAllWithItems()) {
            System.out.println("  주문 " + order.getId() + ": " + order.getMember().getName()
                    + ", " + order.getStatus() + ", " + order.getTotalPrice() + "원");
        }
        System.out.println("findByStatusWithMember(ORDERED):");
        for (Order order : orderRepository.findByStatusWithMember(OrderStatus.ORDERED)) {
            System.out.println("  주문 " + order.getId() + ": " + order.getMember().getName());
        }
    }
}
