package com.sparta.msa_exam.order.orders;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController // RESTful 웹 서비스의 컨트롤러임을 나타내는 어노테이션
@RequestMapping("/orders") // "/orders" 경로에 대한 요청을 처리
@RequiredArgsConstructor // final 필드에 대한 생성자를 자동 생성
public class OrderController {

    private final OrderService orderService; // 주문 서비스 객체

    @Value("${server.port}") // application.yml에서 서버 포트 값을 주입
    private String serverPort;

    // 주문 생성 API
    @PostMapping // POST 요청을 처리 (주문 생성)
    public OrderDto createOrder(
            @RequestBody OrderDto orderDto, // 요청 본문에서 OrderDto 객체를 가져옴
            HttpServletResponse response // HTTP 응답 객체
    ) {
        addServerPortHeader(response); // 응답 헤더에 서버 포트 추가
        return orderService.createOrder(orderDto); // 주문 생성 서비스 호출
    }

    // 주문 ID로 주문 조회 API
    @GetMapping("/{orderId}") // GET 요청을 처리 (특정 주문 조회)
    public OrderDto getOrderById(
            @PathVariable Long orderId, // URL 경로에서 주문 ID를 가져옴
            HttpServletResponse response // HTTP 응답 객체
    ){
        addServerPortHeader(response); // 응답 헤더에 서버 포트 추가
        return orderService.getOrderById(orderId); // 주문 조회 서비스 호출
    }

    // 주문 업데이트 API
    @PutMapping("/{orderId}") // PUT 요청을 처리 (주문 업데이트)
    public OrderDto updateOrder(
            @PathVariable Long orderId, // URL 경로에서 주문 ID를 가져옴
            @RequestBody Long productId, // 요청 본문에서 제품 ID를 가져옴
            HttpServletResponse response // HTTP 응답 객체
    ){
        addServerPortHeader(response); // 응답 헤더에 서버 포트 추가
        return orderService.updateOrder(orderId, productId); // 주문 업데이트 서비스 호출
    }

    // 응답 헤더에 서버 포트 추가하는 메서드
    private void addServerPortHeader(
            HttpServletResponse response // HTTP 응답 객체
    ) {
        response.addHeader(
                "Server-Port", // 헤더 이름
                serverPort); // 헤더 값으로 서버 포트 추가
    }

}
