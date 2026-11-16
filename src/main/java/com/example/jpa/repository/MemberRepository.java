package com.example.jpa.repository;

import com.example.jpa.domain.Member;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

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
}
