package com.sparta.msa_exam.auth;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// REST API 컨트롤러를 정의하는 클래스
@RestController
@RequestMapping("/auth") // "/auth" 경로로 들어오는 요청을 처리
@RequiredArgsConstructor // final 필드를 가진 생성자를 자동 생성
public class AuthController {

    private final AuthService authService; // 인증 서비스 주입

    @Value("${server.port}") // 서버 포트를 설정 파일에서 가져오기
    private String serverPort;

    // 로그인 요청을 처리하는 메서드
    @PostMapping("/signIn") // "/auth/signIn" 경로로 POST 요청을 처리
    public ResponseEntity<?> createAuthenticationToken(
            @RequestBody SignInRequest signInRequest, // 로그인 요청 데이터
            HttpServletResponse response // HTTP 응답 객체
    ){
        // 인증 서비스에서 로그인 수행 후 토큰 생성
        String token = authService.signIn(
                signInRequest.getUserId(),
                signInRequest.getPassword()
        );

        // 서버 포트 정보를 응답 헤더에 추가
        addServerPortHeader(response);

        // 생성된 토큰을 포함한 응답 반환
        return ResponseEntity.ok(new AuthResponse(token));
    }

    // 회원가입 요청을 처리하는 메서드
    @PostMapping("/signUp") // "/auth/signUp" 경로로 POST 요청을 처리
    public ResponseEntity<?> signUp(
            @RequestBody User user, // 사용자 정보
            HttpServletResponse response // HTTP 응답 객체
    ){
        // 인증 서비스에서 사용자 등록 수행
        User createUser = authService.signUp(user);

        // 서버 포트 정보를 응답 헤더에 추가
        addServerPortHeader(response);

        // 생성된 사용자 정보를 포함한 응답 반환
        return ResponseEntity.ok(createUser);
    }

    // 인증 응답을 위한 DTO 클래스
    @Data // Lombok의 @Data 어노테이션으로 getter, setter, toString 등을 자동 생성
    @AllArgsConstructor // 모든 필드를 인자로 받는 생성자 자동 생성
    @NoArgsConstructor // 기본 생성자 자동 생성
    static class AuthResponse {
        private String access_token; // 액세스 토큰
    }

    // 로그인 요청을 위한 DTO 클래스
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class SignInRequest {
        private String userId; // 사용자 ID
        private String password; // 비밀번호
    }

    // 서버 포트 정보를 응답 헤더에 추가하는 메서드
    private void addServerPortHeader(
            HttpServletResponse response // HTTP 응답 객체
    ) {
        response.addHeader(
                "Server-Port",
                serverPort); // "Server-Port" 헤더에 서버 포트 추가
    }
}
