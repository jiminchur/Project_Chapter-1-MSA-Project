package com.sparta.msa_exam.product.products;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service // 서비스 레이어임을 나타내는 어노테이션
@RequiredArgsConstructor // final 필드에 대한 생성자를 자동 생성
public class ProductService {

    private final ProductRepository productRepository; // 제품 리포지토리 주입

    // 상품 생성 메서드
    @CachePut(cacheNames = "productCache", key = "#result.productId") // 생성 후 캐시에 저장
    @CacheEvict(cacheNames = "productAllCache", allEntries = true) // 모든 상품 캐시 무효화
    public ProductDto crateProduct(
            ProductDto productDto // 생성할 상품 DTO
    ){
        // Product 엔티티로 변환하여 저장 후 DTO로 변환하여 반환
        return ProductDto.fromEntity(
                productRepository.save(
                        Product.builder() // 빌더 패턴을 사용하여 Product 객체 생성
                                .name(productDto.getName()) // 이름 설정
                                .supplyPrice(productDto.getSupplyPrice()) // 공급 가격 설정
                                .build() // Product 객체 빌드
                )
        );
    }

    // 모든 상품 조회 메서드
    @Cacheable(cacheNames = "productAllCache", key = "methodName") // 결과를 캐시에 저장
    public List<ProductDto> readAllProduct() {
        // 모든 상품을 조회하고 DTO로 변환하여 반환
        return productRepository.findAll()
                .stream()
                .map(ProductDto::fromEntity) // Product 엔티티를 ProductDto로 변환
                .toList(); // List로 변환하여 반환
    }

    // ID로 상품 조회 메서드
    public ProductDto findProductById(Long productId) {
        // ID로 상품을 찾고, 존재하지 않으면 404 오류 발생
        return productRepository.findById(productId)
                .map(ProductDto::fromEntity) // Product 엔티티를 ProductDto로 변환
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND) // 상품이 없을 경우 예외 발생
                );
    }
}