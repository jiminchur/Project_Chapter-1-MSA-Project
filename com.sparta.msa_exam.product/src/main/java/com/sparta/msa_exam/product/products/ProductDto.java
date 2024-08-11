package com.sparta.msa_exam.product.products;

import lombok.*;

import java.io.Serializable;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto implements Serializable {
    private Long productId;
    private String name;
    private Integer supplyPrice;

    // Product 엔티티를 ProductDto로 변환하는 정적 메서드
    public static ProductDto fromEntity(
            Product product // 변환할 Product 엔티티
    ){
        return ProductDto.builder() // 빌더를 사용하여 ProductDto 객체 생성
                .productId(product.getProductId()) // Product의 제품 ID 설정
                .name(product.getName()) // Product의 제품 이름 설정
                .supplyPrice(product.getSupplyPrice()) // Product의 공급 가격 설정
                .build(); // ProductDto 객체 빌드 및 반환
    }
}
