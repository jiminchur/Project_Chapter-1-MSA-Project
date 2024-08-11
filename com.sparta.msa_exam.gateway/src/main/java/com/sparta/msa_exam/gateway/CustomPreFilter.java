package com.sparta.msa_exam.gateway;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.logging.Logger;

// 글로벌 프리 필터 클래스
@Component // 이 클래스가 스프링의 컴포넌트로 관리됨을 나타냄
public class CustomPreFilter implements GlobalFilter, Ordered {

    private static final Logger logger = Logger.getLogger(CustomPreFilter.class.getName()); // Logger 인스턴스 생성

    // 필터 메서드 구현
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest(); // 현재 요청 객체 가져오기
        logger.info("Pre Filter: Request URI is " + request.getURI()); // 요청 URI 로그 출력
        return chain.filter(exchange); // 다음 필터로 요청 전달
    }

    // 필터의 우선순위를 정의
    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE; // 가장 높은 우선순위로 설정 (가장 먼저 실행됨)
    }
}
