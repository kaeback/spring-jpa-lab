package com.example.jpa.demo;

import com.example.jpa.domain.Grade;
import com.example.jpa.domain.Member;
import com.example.jpa.domain.Product;
import com.example.jpa.service.MemberService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * step2: 영속성 컨텍스트 — 쓰기 지연, 변경 감지, 삭제, 준영속과 merge
 */
@Component
@RequiredArgsConstructor
public class Step2Demo {

    private final MemberService memberService;
    private final EntityManager em;

    /** [2-1] PK 생성 전략과 INSERT 시점 — IDENTITY는 바로, SEQUENCE는 flush할 때 */
    @Transactional
    public void idStrategy() {
        Demo.title("[2-1] PK 생성 전략과 INSERT 시점");
        System.out.println("Member persist (IDENTITY)");
        em.persist(new Member("김철수", "kim@test.com"));   // id를 DB가 만들므로 INSERT를 바로 실행한다

        System.out.println("Product persist (SEQUENCE)");
        Product keyboard = new Product("키보드", 50000, 10);
        Product mouse = new Product("마우스", 20000, 20);
        em.persist(keyboard);   // 시퀀스에서 id만 받아 온다 — INSERT는 미룬다
        em.persist(mouse);
        System.out.println("상품 id: " + keyboard.getId() + ", " + mouse.getId() + " — 아직 INSERT 전");

        System.out.println("flush 호출");
        em.flush();             // 쌓아 둔 INSERT를 DB에 보낸다
        System.out.println("flush 끝");
    }

    /** [2-2] 변경 감지 — 서비스는 값만 바꾸고, 커밋할 때 UPDATE가 실행된다 */
    public void dirtyChecking() {
        Demo.title("[2-2] 변경 감지");
        System.out.println("변경 전: " + memberService.findMember(1L));
        System.out.println("changeGrade(1, VIP) 호출");
        memberService.changeGrade(1L, Grade.VIP);
        System.out.println("changeGrade 반환 — 커밋 끝");
        System.out.println("변경 후: " + memberService.findMember(1L));
    }

    /** [2-3] 쓰기 지연 — UPDATE와 DELETE를 모아 두었다가 flush할 때 실행한다 */
    @Transactional
    public void writeBehind() {
        Demo.title("[2-3] 쓰기 지연");
        Product keyboard = em.find(Product.class, 1L);
        Product mouse = em.find(Product.class, 2L);

        keyboard.changePrice(55000);   // UPDATE 예약
        em.remove(mouse);              // DELETE 예약
        System.out.println("가격 변경과 삭제를 호출했다 — 아직 SQL 없음");

        System.out.println("flush 호출");
        em.flush();
        System.out.println("flush 끝 — 커밋은 메서드가 끝날 때");
    }

    /** [2-4] 삭제 — 서비스로 회원을 가입시켰다가 탈퇴시킨다 */
    public void remove() {
        Demo.title("[2-4] 회원 탈퇴 (삭제)");
        Long id = memberService.join("이영희", "lee@test.com");
        System.out.println("가입: id=" + id);
        memberService.withdraw(id);
        System.out.println("탈퇴 후 조회");
        try {
            memberService.findMember(id);
        } catch (IllegalArgumentException e) {
            System.out.println("예외: " + e.getMessage());
        }
    }

    /** [2-5] 준영속과 merge — 영속성 컨텍스트에서 분리된 엔티티는 변경 감지가 되지 않는다 */
    @Transactional
    public void detachAndMerge() {
        Demo.title("[2-5] 준영속과 merge");
        Product keyboard = em.find(Product.class, 1L);
        em.detach(keyboard);           // 준영속: 영속성 컨텍스트가 더 이상 관리하지 않는다
        keyboard.changePrice(60000);
        System.out.println("detach 후 가격 변경 — 영속 상태? " + em.contains(keyboard));
        em.flush();
        System.out.println("flush 했지만 UPDATE 없음");

        Product merged = em.merge(keyboard);   // 같은 id의 영속 엔티티를 찾아 값을 복사하고 그 엔티티를 돌려준다
        System.out.println("merge 후 — merged 영속 상태? " + em.contains(merged)
                + ", keyboard == merged ? " + (keyboard == merged));
        em.flush();
        System.out.println("flush 끝");
    }
}
