package com.example.jpa.repository;

import com.example.jpa.domain.Product;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 상품 리포지토리
 */
@Repository
@RequiredArgsConstructor
public class ProductRepository {

    private final EntityManager em;

    public void save(Product product) {
        em.persist(product);
    }

    public Product findById(Long id) {
        return em.find(Product.class, id);
    }

    /** 상품 수 — 결과가 하나이므로 getSingleResult() */
    public long count() {
        return em.createQuery("select count(p) from Product p", Long.class)
                .getSingleResult();
    }

    /** 이름에 keyword가 들어간 상품 — :keyword는 이름 있는 파라미터 */
    public List<Product> findByNameContaining(String keyword) {
        return em.createQuery("select p from Product p where p.name like :keyword order by p.id", Product.class)
                .setParameter("keyword", "%" + keyword + "%")
                .getResultList();
    }

    /** 가격이 min 이상 max 이하인 상품을 가격 순으로 */
    public List<Product> findByPriceBetween(int min, int max) {
        return em.createQuery(
                        "select p from Product p where p.price between :min and :max order by p.price",
                        Product.class)
                .setParameter("min", min)
                .setParameter("max", max)
                .getResultList();
    }
}
