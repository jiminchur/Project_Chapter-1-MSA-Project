package com.sparta.msa_exam.order.orders;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto implements Serializable { // OrderDto 클래스는 직렬화 가능
    private Long orderId;
    private String name;
    private List<Long> productIds;

    // Order 엔티티를 OrderDto로 변환하는 정적 메서드
    public static OrderDto fromEntity(Order order) {
        return OrderDto.builder() // 빌더를 사용하여 OrderDto 객체 생성
                .orderId(order.getOrderId()) // Order의 주문 ID 설정
                .name(order.getName()) // Order의 주문자 이름 설정
                .productIds(order.getProductIds()) // Order의 제품 ID 리스트 설정
                .build(); // OrderDto 객체 빌드 및 반환
    }
}
