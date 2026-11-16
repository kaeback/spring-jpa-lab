package com.example.jpa.demo;

import com.example.jpa.domain.Product;
import com.example.jpa.repository.ProductRepository;
import com.example.jpa.service.MemberService;
import com.example.jpa.service.ProductService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * step3: JPQL — 엔티티를 대상으로 하는 조회
 */
@Component
@RequiredArgsConstructor
public class Step3Demo {

    private final MemberService memberService;
    private final ProductService productService;
    private final ProductRepository productRepository;
    private final EntityManager em;

    /** 실습 데이터 — 회원 3명, 상품 5개 */
    public void prepare() {
        Demo.title("실습 데이터 준비");
        memberService.join("김철수", "kim@test.com");
        memberService.join("이영희", "lee@test.com");
        memberService.join("박민수", "park@test.com");
        productService.register("키보드", 50000, 10);
        productService.register("마우스", 20000, 20);
        productService.register("모니터", 300000, 5);
        productService.register("노트북", 1500000, 3);
        productService.register("무선 키보드", 70000, 7);
    }

    /** [3-1] 전체 조회와 개수 */
    public void findAll() {
        Demo.title("[3-1] 전체 조회와 개수");
        memberService.findMembers().forEach(System.out::println);
        System.out.println("상품 수: " + productService.countProducts());
    }

    /** [3-2] 이름으로 검색 — like와 파라미터 바인딩 */
    public void searchByName() {
        Demo.title("[3-2] 이름에 '키보드'가 들어간 상품");
        productService.searchByName("키보드").forEach(System.out::println);
    }

    /** [3-3] 가격 범위로 검색 — between과 order by */
    public void searchByPrice() {
        Demo.title("[3-3] 3만~10만 원 상품 (가격 순)");
        productService.searchByPrice(30000, 100000).forEach(System.out::println);
    }

    /** [3-4] JPQL을 실행하기 전에 자동으로 flush한다 */
    @Transactional
    public void autoFlush() {
        Demo.title("[3-4] JPQL 실행 전 자동 flush");
        Product mouse = em.find(Product.class, 2L);
        mouse.changePrice(40000);
        System.out.println("마우스 가격을 40000으로 바꿨다 — 아직 UPDATE 전");
        System.out.println("JPQL 실행");
        productRepository.findByPriceBetween(30000, 100000).forEach(System.out::println);
    }
}
