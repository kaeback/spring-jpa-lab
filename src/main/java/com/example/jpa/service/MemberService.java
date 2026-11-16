package com.example.jpa.service;

import com.example.jpa.domain.Grade;
import com.example.jpa.domain.Member;
import com.example.jpa.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 회원 서비스 — 메서드 하나가 트랜잭션 하나다
 */
@Service
@Transactional   // public 메서드마다 트랜잭션을 시작하고, 끝나면 커밋한다 (예외가 발생하면 롤백)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    /** 회원 가입 — 저장한 회원의 id를 돌려준다 */
    public Long join(String name, String email) {
        Member member = new Member(name, email);
        memberRepository.save(member);
        return member.getId();
    }

    /** 회원 조회 — 없으면 예외 */
    @Transactional(readOnly = true)   // 조회만 하는 트랜잭션
    public Member findMember(Long id) {
        Member member = memberRepository.findById(id);
        if (member == null) {
            throw new IllegalArgumentException("회원이 없습니다. id=" + id);
        }
        return member;
    }

    /** 전체 회원 */
    @Transactional(readOnly = true)
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    /** 등급 변경 — 값만 바꾸고 저장 메서드는 호출하지 않는다 (변경 감지) */
    public void changeGrade(Long id, Grade grade) {
        Member member = findMember(id);
        member.changeGrade(grade);
    }

    /** 회원 탈퇴 */
    public void withdraw(Long id) {
        Member member = findMember(id);
        memberRepository.delete(member);
    }
}
