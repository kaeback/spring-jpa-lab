package com.example.jpa.repository;

import com.example.jpa.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 상품 리포지토리
 */
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByNameContainingOrderByIdAsc(String keyword);        // where name like %?%

    List<Product> findByPriceBetweenOrderByPriceAsc(int min, int max);     // where price between ? and ?
}
