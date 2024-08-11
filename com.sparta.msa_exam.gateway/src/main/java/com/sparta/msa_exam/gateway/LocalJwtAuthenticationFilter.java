package com.sparta.msa_exam.gateway;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;

@Component // 이 클래스가 스프링의 컴포넌트로 관리됨을 나타냄
public class LocalJwtAuthenticationFilter implements GlobalFilter {

    @Value("${service.jwt.secret-key}") // application.properties에서 secretKey 값을 주입
    private String secretKey;

    // 필터 메서드 구현
    @Override
    public Mono<Void> filter(
            ServerWebExchange exchange, GatewayFilterChain chain
    ){
        String path = exchange
                .getRequest()
                .getURI()
                .getPath(); // 요청 URI의 경로 가져오기

        // 인증이 필요 없는 경로 처리
        if (path.equals("/auth/signIn") || path.equals("/auth/signUp")) {
            return chain.filter(exchange);  // /signIn 및 /signUp 경로는 필터를 적용하지 않음
        }

        // JWT 토큰 추출
        String token = extractToken(exchange);

        // 토큰이 없거나 유효하지 않은 경우
        if (token == null || !validateToken(token, exchange)) {
            exchange
                    .getResponse()
                    .setStatusCode(HttpStatus.UNAUTHORIZED); // 401 상태 코드 설정
            return exchange
                    .getResponse()
                    .setComplete(); // 응답 완료
        }

        return chain.filter(exchange); // 필터 체인 계속 진행
    }

    // Authorization 헤더에서 JWT 토큰 추출
    private String extractToken(
            ServerWebExchange exchange
    ){
        String authHeader = exchange
                .getRequest()
                .getHeaders()
                .getFirst("Authorization"); // Authorization 헤더 가져오기
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7); // "Bearer " 이후의 토큰 반환
        }
        return null; // 토큰이 없으면 null 반환
    }

    // JWT 토큰 유효성 검증
    private boolean validateToken(
            String token,
            ServerWebExchange exchange
    ){
        try {
            SecretKey key = Keys.hmacShaKeyFor(
                    Decoders
                            .BASE64URL
                            .decode(secretKey) // 비밀 키를 디코딩하여 생성
            );
            Jws<Claims> claimsJws = Jwts.parser()
                    .verifyWith(key) // 키로 서명 검증
                    .build()
                    .parseSignedClaims(token); // 토큰의 클레임 파싱
            Claims claims = claimsJws.getBody(); // 클레임 본문 가져오기
            exchange.getRequest().mutate()
                    .header(
                            "X-User-Id", // 요청 헤더에 사용자 ID 추가
                            claims.get("user_id")
                                    .toString()
                    )
                    .build();
            // 추가적인 검증 로직 (예: 토큰 만료 여부 확인 등)을 여기에 추가할 수 있습니다.
            return true; // 유효한 토큰인 경우 true 반환
        } catch (Exception e) {
            return false; // 예외 발생 시 false 반환
        }
    }
}
