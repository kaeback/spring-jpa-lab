package com.example.jpa.demo;

import com.example.jpa.domain.Member;
import com.example.jpa.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * step1: 엔티티 저장과 조회, 영속성 컨텍스트의 1차 캐시
 * 메서드 하나가 트랜잭션 하나다. 메서드가 끝나면 커밋되고 영속성 컨텍스트도 사라진다.
 */
@Component
@RequiredArgsConstructor
public class Step1Demo {

    private final MemberRepository memberRepository;
    private final EntityManager em;

    /** [1-1] 저장 — persist하면 영속 상태가 된다. 저장한 회원의 id를 돌려준다 */
    @Transactional
    public Long save() {
        Demo.title("[1-1] 회원 저장 — 비영속에서 영속으로");
        Member member = new Member("김철수", "kim@test.com");
        System.out.println("저장 전: " + member + " / 영속 상태? " + em.contains(member));

        memberRepository.save(member);   // em.persist(member)
        System.out.println("저장 후: " + member + " / 영속 상태? " + em.contains(member));
        return member.getId();
    }

    /** [1-2] 조회 — 새 트랜잭션에서 find하면 DB에서 SELECT한다 */
    @Transactional
    public void find(Long id) {
        Demo.title("[1-2] 회원 조회");
        Member member = memberRepository.findById(id);
        System.out.println("조회 결과: " + member);
        System.out.println("없는 id 조회: " + memberRepository.findById(999L));
    }

    /** [1-3] 1차 캐시와 동일성 — 같은 id를 두 번 조회해도 SELECT는 한 번 */
    @Transactional
    public void firstLevelCache(Long id) {
        Demo.title("[1-3] 1차 캐시와 동일성");
        System.out.println("첫 번째 조회");
        Member first = memberRepository.findById(id);    // 1차 캐시에 없다 → SELECT
        System.out.println("두 번째 조회");
        Member second = memberRepository.findById(id);   // 1차 캐시에 있다 → SELECT 없음
        System.out.println("first == second ? " + (first == second));
    }

    /** [1-4] 저장한 엔티티도 1차 캐시에 들어간다 — 같은 트랜잭션에서 조회하면 SELECT가 없다 */
    @Transactional
    public void findAfterSave() {
        Demo.title("[1-4] 저장한 뒤 같은 트랜잭션에서 조회");
        Member member = new Member("이영희", "lee@test.com");
        memberRepository.save(member);
        System.out.println("저장한 회원 조회");
        Member found = memberRepository.findById(member.getId());
        System.out.println("found == member ? " + (found == member));
    }
}
