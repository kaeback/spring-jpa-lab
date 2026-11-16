package com.example.jpa.demo;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * step0: DB 연결 확인
 */
@Component
@RequiredArgsConstructor
public class Step0Demo {

    private final EntityManager em;   // JPA로 DB를 다루는 객체 (스프링이 주입한다)

    /** [0-1] DB에 SQL을 보내 PostgreSQL 버전을 받아 온다 */
    @Transactional
    public void checkConnection() {
        Demo.title("[0-1] DB 연결 확인");
        Object version = em.createNativeQuery("select version()").getSingleResult();
        System.out.println("연결 성공: " + version);
    }
}
