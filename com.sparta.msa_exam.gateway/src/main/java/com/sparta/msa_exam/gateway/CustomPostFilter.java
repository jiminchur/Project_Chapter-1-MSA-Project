package com.sparta.msa_exam.gateway;

import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.logging.Logger;

// 글로벌 포스트 필터 클래스
@Component // 이 클래스가 스프링의 컴포넌트로 관리됨을 나타냄
public class CustomPostFilter implements GlobalFilter, Ordered {

    private static final Logger logger = Logger.getLogger(CustomPostFilter.class.getName()); // Logger 인스턴스 생성

    // 필터 메서드 구현
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, org.springframework.cloud.gateway.filter.GatewayFilterChain chain) {
        // 다음 필터를 호출하고, 그 후에 실행될 로직을 정의
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            ServerHttpResponse response = exchange.getResponse(); // 현재 응답 객체 가져오기
            logger.info("Post Filter: Response status code is " + response.getStatusCode()); // 응답 상태 코드 로그 출력
        }));
    }

    // 필터의 우선순위를 정의
    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE; // 가장 낮은 우선순위로 설정 (다른 필터들 뒤에 실행)
    }
}
