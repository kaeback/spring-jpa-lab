package com.example.jpa.demo;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 애플리케이션이 시작되면 이 단계의 데모 메서드를 차례로 호출한다.
 * 콘솔에서 데모 출력과 SQL 로그(Hibernate: ...)를 함께 확인한다.
 */
@Component
@RequiredArgsConstructor
public class DemoRunner implements CommandLineRunner {

    private final Step2Demo step2;

    @Override
    public void run(String... args) {
        step2.idStrategy();
        step2.dirtyChecking();
        step2.writeBehind();
        step2.remove();
        step2.detachAndMerge();
    }
}
