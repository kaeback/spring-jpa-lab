package com.example.jpa.repository;

import com.example.jpa.domain.Order;
import com.example.jpa.domain.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 주문 리포지토리
 */
public interface OrderRepository extends JpaRepository<Order, Long> {

    // 연관된 엔티티의 필드는 이름을 이어 붙인다: member.id → MemberId
    List<Order> findByMemberIdOrderByIdAsc(Long memberId);

    // @Query — 메서드 이름으로 표현하기 어려운 JPQL은 직접 쓴다 (fetch join)
    @Query("select o from Order o"
            + " join fetch o.member"
            + " join fetch o.orderItems oi"
            + " join fetch oi.product"
            + " order by o.id")
    List<Order> findAllWithItems();

    // :status 자리에 @Param("status")의 값이 들어간다
    @Query("select o from Order o join fetch o.member where o.status = :status order by o.id")
    List<Order> findByStatusWithMember(@Param("status") OrderStatus status);
}
