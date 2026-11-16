package com.example.jpa.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 회원 엔티티 — member 테이블과 매핑한다
 */
@Entity                      // JPA가 관리하는 엔티티
@Table(name = "member")      // 매핑할 테이블 이름 (생략하면 엔티티 이름을 쓴다)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)   // JPA가 객체를 만들 때 쓰는 기본 생성자
public class Member {

    @Id                                                   // 기본 키(PK)
    @GeneratedValue(strategy = GenerationType.IDENTITY)   // PK 값은 DB가 만든다 (PostgreSQL identity 컬럼)
    private Long id;

    @Column(nullable = false, length = 20)                // NOT NULL, varchar(20)
    private String name;

    @Column(nullable = false, unique = true, length = 50) // 같은 이메일은 저장할 수 없다
    private String email;

    @Enumerated(EnumType.STRING)                          // enum을 이름(BASIC, VIP)으로 저장한다
    @Column(nullable = false, length = 10)
    private Grade grade;

    @Column(nullable = false, updatable = false)          // 처음 저장한 뒤에는 UPDATE하지 않는다
    private LocalDateTime createdAt;                      // 컬럼 이름은 created_at이 된다

    public Member(String name, String email) {
        this.name = name;
        this.email = email;
        this.grade = Grade.BASIC;
        this.createdAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Member{id=" + id + ", name=" + name + ", email=" + email + ", grade=" + grade + "}";
    }
}
