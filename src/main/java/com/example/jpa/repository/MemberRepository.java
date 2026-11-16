package com.example.jpa.repository;

import com.example.jpa.domain.Member;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 회원 리포지토리 — EntityManager로 Member를 저장하고 조회한다
 */
@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;

    /** 저장 — 영속성 컨텍스트에 넣는다 (persist) */
    public void save(Member member) {
        em.persist(member);
    }

    /** id로 조회 — 없으면 null */
    public Member findById(Long id) {
        return em.find(Member.class, id);
    }

    /** 전체 조회 — JPQL: 테이블이 아니라 엔티티(Member)를 대상으로 쓴다 */
    public List<Member> findAll() {
        return em.createQuery("select m from Member m order by m.id", Member.class)
                .getResultList();
    }

    /** 삭제 — 영속 상태의 엔티티를 삭제 상태로 만든다 (remove) */
    public void delete(Member member) {
        em.remove(member);
    }
}
