package com.example.jpa.service;

import com.example.jpa.domain.Member;
import com.example.jpa.domain.Order;
import com.example.jpa.domain.OrderItem;
import com.example.jpa.domain.Product;
import com.example.jpa.repository.OrderItemRepository;
import com.example.jpa.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 주문 서비스
 */
@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final MemberService memberService;
    private final ProductService productService;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    /** 주문 — 회원이 상품 하나를 quantity개 주문한다. 주문 id를 돌려준다 */
    public Long order(Long memberId, Long productId, int quantity) {
        Member member = memberService.findMember(memberId);
        Product product = productService.findProduct(productId);

        Order order = new Order(member);
        orderRepository.save(order);

        OrderItem orderItem = new OrderItem(order, product, quantity);   // 재고가 부족하면 여기서 예외
        orderItemRepository.save(orderItem);
        return order.getId();
    }

    /** 주문 수 */
    @Transactional(readOnly = true)
    public long countOrders() {
        return orderRepository.count();
    }
}
