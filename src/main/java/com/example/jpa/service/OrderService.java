package com.example.jpa.service;

import com.example.jpa.domain.Member;
import com.example.jpa.domain.Order;
import com.example.jpa.domain.OrderItem;
import com.example.jpa.domain.Product;
import com.example.jpa.dto.OrderDto;
import com.example.jpa.dto.OrderSummary;
import com.example.jpa.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    /** 주문 — 회원이 여러 상품을 한 번에 주문한다. 주문 id를 돌려준다 */
    public Long order(Long memberId, List<OrderLine> lines) {
        Member member = memberService.findMember(memberId);
        Order order = new Order(member);
        for (OrderLine line : lines) {
            Product product = productService.findProduct(line.productId());
            order.addOrderItem(new OrderItem(product, line.quantity()));   // 재고가 부족하면 예외
        }
        orderRepository.save(order);   // 주문만 저장해도 cascade로 주문 상품까지 저장된다
        return order.getId();
    }

    /** 주문 조회 — 없으면 예외 */
    @Transactional(readOnly = true)
    public Order findOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문이 없습니다. id=" + orderId));
    }

    /** 주문 조회 — 트랜잭션 안에서 DTO로 바꿔 돌려준다 */
    @Transactional(readOnly = true)
    public OrderDto findOrderDto(Long orderId) {
        return OrderDto.from(findOrder(orderId));
    }

    /** 주문 요약 목록 — JPQL로 DTO를 바로 조회한다 */
    @Transactional(readOnly = true)
    public List<OrderSummary> findOrderSummaries() {
        return orderRepository.findOrderSummaries();
    }

    /** 회원의 주문 목록 */
    @Transactional(readOnly = true)
    public List<Order> findOrdersByMember(Long memberId) {
        return orderRepository.findByMemberIdOrderByIdAsc(memberId);
    }

    /** 전체 주문 (fetch join으로 회원·주문 상품·상품까지) */
    @Transactional(readOnly = true)
    public List<Order> findOrdersWithItems() {
        return orderRepository.findAllWithItems();
    }

    /** 주문 취소 — 상태 변경과 재고 복구는 변경 감지로 UPDATE된다 */
    public void cancel(Long orderId) {
        findOrder(orderId).cancel();
    }

    /** 주문에서 상품 하나를 뺀다 */
    public void removeOrderItem(Long orderId, Long productId) {
        Order order = findOrder(orderId);
        OrderItem target = null;
        for (OrderItem orderItem : order.getOrderItems()) {
            if (orderItem.getProduct().getId().equals(productId)) {
                target = orderItem;
            }
        }
        if (target == null) {
            throw new IllegalArgumentException("주문에 없는 상품입니다. productId=" + productId);
        }
        order.removeOrderItem(target);
    }

    /** 주문 수 */
    @Transactional(readOnly = true)
    public long countOrders() {
        return orderRepository.count();
    }
}
