package com.sparta.msa_exam.order.orders;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본 키의 자동 증가 전략 설정
    private Long orderId;
    private String name;

    // 주문에 포함된 제품 ID 리스트
    @ElementCollection // 기본 타입의 컬렉션을 매핑하기 위한 어노테이션
    @CollectionTable(name = "order_items", joinColumns = @JoinColumn(name = "order_id")) // 'order_items' 테이블과 매핑
    @Column(name = "product_id") // 컬렉션 테이블의 'product_id' 컬럼과 매핑
    private List<Long> productIds; // 제품 ID 리스트
}
