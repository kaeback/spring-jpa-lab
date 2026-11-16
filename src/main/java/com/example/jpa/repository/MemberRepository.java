package com.example.jpa.repository;

import com.example.jpa.domain.Grade;
import com.example.jpa.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * 회원 리포지토리 — 인터페이스만 선언하면 Spring Data JPA가 구현 객체를 만든다
 * save, findById, findAll, count, delete 등은 JpaRepository에 이미 있다.
 */
public interface MemberRepository extends JpaRepository<Member, Long> {

    // 쿼리 메서드 — 메서드 이름을 보고 JPQL을 만든다
    Optional<Member> findByEmail(String email);          // where email = ?

    List<Member> findByGradeOrderByIdAsc(Grade grade);   // where grade = ? order by id

    List<Member> findAllByOrderByIdAsc();                // order by id
}
