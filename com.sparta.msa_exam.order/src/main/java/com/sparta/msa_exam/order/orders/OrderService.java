package com.sparta.msa_exam.order.orders;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service // 이 클래스가 서비스 컴포넌트임을 나타냄
@RequiredArgsConstructor // final 필드에 대한 생성자를 자동 생성
public class OrderService {

    private final ProductClient productClient; // 상품 정보를 가져오는 클라이언트
    private final OrderRepository orderRepository; // 주문 정보를 저장하고 조회하는 레포지토리

    // 주문 ID로 주문 조회하는 메서드
    public OrderDto getOrderById(
            Long orderId // 조회할 주문 ID
    ){
        return orderRepository.findById(orderId) // 주문 ID로 주문을 조회
                .map(OrderDto::fromEntity) // 주문이 존재하면 OrderDto로 변환
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND) // 주문이 없으면 404 오류 발생
                );
    }

    // 주문 업데이트 메서드
    public OrderDto updateOrder(
            Long orderId, // 업데이트할 주문 ID
            Long productId // 추가할 상품 ID
    ){
        ProductDto product = productClient.getProduct(productId); // 상품 정보를 가져옴

        // 상품이 존재하지 않으면 BAD_REQUEST 오류 발생
        if (product == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "상품이 존재하지 않습니다.");
        }

        Order order = orderRepository.findById(orderId) // 주문 ID로 주문 조회
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND) // 주문이 없으면 404 오류 발생
                );
        order.getProductIds().add(productId); // 주문에 상품 ID 추가
        return OrderDto.fromEntity(orderRepository.save(order)); // 업데이트된 주문을 저장하고 OrderDto로 반환
    }

    // 주문 생성 메서드
    public OrderDto createOrder(
            OrderDto orderDto // 생성할 주문 정보
    ){
        // 모든 상품 ID가 유효한지 확인
        for (Long productId : orderDto.getProductIds()) {
            try {
                productClient.getProduct(productId); // 상품 정보를 가져옴
            } catch (FeignException.NotFound e) {
                // 상품이 존재하지 않으면 BAD_REQUEST 오류 발생
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "상품 ID " + productId + "는 존재하지 않습니다.");
            }
        }

        // 주문을 생성하여 저장하고 OrderDto로 반환
        return OrderDto.fromEntity(
                orderRepository.save(
                        Order.builder() // 빌더 패턴을 사용하여 새로운 주문 객체 생성
                                .orderId(orderDto.getOrderId()) // 주문 ID 설정
                                .name(orderDto.getName()) // 주문자 이름 설정
                                .productIds(orderDto.getProductIds()) // 제품 ID 리스트 설정
                                .build() // 주문 객체 빌드
                )
        );
    }
}
