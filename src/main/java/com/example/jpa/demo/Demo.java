package com.example.jpa.demo;

/**
 * 데모 출력을 돕는 메서드 모음
 */
public final class Demo {

    private Demo() {
    }

    /** 데모 하나의 시작을 표시한다 */
    public static void title(String title) {
        System.out.println();
        System.out.println("===== " + title + " =====");
    }
}
