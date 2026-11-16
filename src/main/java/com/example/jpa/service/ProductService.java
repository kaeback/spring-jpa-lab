package com.example.jpa.service;

import com.example.jpa.domain.Product;
import com.example.jpa.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 상품 서비스
 */
@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    /** 상품 등록 — 저장한 상품의 id를 돌려준다 */
    public Long register(String name, int price, int stockQuantity) {
        Product product = new Product(name, price, stockQuantity);
        productRepository.save(product);
        return product.getId();
    }

    /** 상품 조회 — 없으면 예외 */
    @Transactional(readOnly = true)
    public Product findProduct(Long id) {
        Product product = productRepository.findById(id);
        if (product == null) {
            throw new IllegalArgumentException("상품이 없습니다. id=" + id);
        }
        return product;
    }

    /** 가격 변경 (변경 감지) */
    public void changePrice(Long id, int price) {
        findProduct(id).changePrice(price);
    }
}
