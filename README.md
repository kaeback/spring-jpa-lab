# spring-jpa-lab — JPA 단계별 실습

자바 백엔드 강의 **JPA 기초**·**Spring Data JPA** 장의 실습 프로젝트입니다.
회원 · 상품 · 주문을 주제로, 단계마다 JPA 기능을 하나씩 추가하고 데모 메서드를 호출해 동작을 확인합니다.

- Java 21 / Spring Boot 4.1 / Spring Data JPA (Hibernate 7) / Lombok
- 웹 서버 없이 실행하는 콘솔 프로그램입니다.
- DB: 로컬 **PostgreSQL**의 `springboot` 데이터베이스와 계정 (프로젝트 생성 장에서 만든 것)
- 실행할 때마다 `ddl-auto: create`로 테이블을 새로 만들므로 항상 같은 상태에서 시작합니다.

## 단계 (브랜치)

| 브랜치 | 추가되는 코드 | 확인하는 주제 |
|---|---|---|
| `step0-setup` | 프로젝트 설정, `DemoRunner` | DB 연결, SQL 로그 |
| `step1-entity` | `Member`, `Grade`, `MemberRepository` | 엔티티 매핑, `persist`·`find`, 1차 캐시와 동일성 |
| `step2-persistence` | `Product`(SEQUENCE), `MemberService`·`ProductService` | PK 생성 전략과 INSERT 시점, 변경 감지, 쓰기 지연, 삭제, 준영속과 merge |
| `step3-jpql` | 리포지토리 검색 메서드 | JPQL, 파라미터 바인딩, 자동 flush |
| `step4-manytoone` | `Order`, `OrderItem`, `OrderService` | `@ManyToOne` 단방향, 지연 로딩과 프록시, 예외와 롤백 |
| `step5-onetomany` | `Order.orderItems`, 주문 취소 | 양방향 연관관계와 연관관계의 주인, cascade, orphanRemoval |
| `step6-fetch` | fetch join 조회 | N+1 문제, fetch join, `default_batch_fetch_size` |
| `step7-spring-data` | 리포지토리를 `JpaRepository`로 교체 | Spring Data JPA, 쿼리 메서드, `@Query` |
| `step8-dto` | `OrderDto`, `OrderSummary` | 트랜잭션 밖 지연 로딩 예외, DTO 변환, `select new` |

`master`는 마지막 단계(`step8-dto`)와 같습니다.

```bash
git checkout step3-jpql    # 원하는 단계로 이동
```

## 실행

1. PostgreSQL에 `springboot` 데이터베이스와 계정(비밀번호 `springboot1234`)이 있어야 합니다.
   다른 값을 쓰면 `application.yml`을 고치거나 환경변수 `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`로 넘깁니다.
2. `JpaLabApplication`을 실행합니다.
   - IntelliJ: `main` 왼쪽의 ▶ 버튼
   - 터미널: `./gradlew -q bootRun` (Windows: `gradlew.bat -q bootRun`)

시작하면 `DemoRunner`가 그 단계의 데모 클래스(`demo/StepNDemo`)의 메서드를 차례로 호출합니다.
콘솔에서 데모 출력과 실행된 SQL(`Hibernate: ...`), `?`에 들어간 값(`binding parameter ...`)을 함께 확인합니다.

## 패키지

```
com.example.jpa
├── domain      엔티티 (Member, Product, Order, OrderItem) 와 enum
├── repository  리포지토리 (step1~6: EntityManager, step7~: JpaRepository)
├── service     서비스 (@Transactional)
├── dto         조회 결과 DTO (step8)
└── demo        DemoRunner, 단계별 데모 클래스
```
