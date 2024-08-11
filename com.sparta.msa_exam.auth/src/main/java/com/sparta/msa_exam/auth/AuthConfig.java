package com.sparta.msa_exam.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

// Spring Security 설정을 위한 구성 클래스
@Configuration
@EnableWebSecurity // 웹 보안 기능 활성화
public class AuthConfig {

    // SecurityFilterChain을 설정하는 메서드
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http // HTTP 보안 설정을 위한 객체
    ) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // CSRF 보호 비활성화 (API 서버에서는 일반적)
                .authorizeRequests(
                        authorize -> authorize
                                // 로그인 요청은 인증 없이 접근 가능
                                .requestMatchers("auth/signIn").permitAll()
                                // 회원가입 요청은 인증 없이 접근 가능
                                .requestMatchers("auth/signUp").permitAll()
                                // 그 외 모든 요청은 인증 필요
                                .anyRequest().authenticated()
                )
                .sessionManagement(
                        session -> session
                                // 세션 관리를 무상태(stateless)로 설정
                                .sessionCreationPolicy(
                                        SessionCreationPolicy.STATELESS
                                )
                );
        return http.build(); // 설정한 보안 체인을 반환
    }

    // 비밀번호 인코더를 설정하는 메서드
    @Bean
    public PasswordEncoder passwordEncoder() {
        // BCrypt 알고리즘을 사용하여 비밀번호를 안전하게 인코딩
        return new BCryptPasswordEncoder();
    }
}
