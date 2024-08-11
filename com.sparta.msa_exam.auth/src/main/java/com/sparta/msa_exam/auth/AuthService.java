package com.sparta.msa_exam.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class AuthService {
    // 애플리케이션 이름을 JWT 발행자(issuer)로 사용하기 위한 설정
    @Value("${spring.application.name}")
    private String issuer;

    // 액세스 토큰의 만료 시간을 설정하기 위한 값
    @Value("${service.jwt.access-expiration}")
    private Long accessExpiration;

    // JWT를 생성하기 위한 비밀 키
    private final SecretKey secretKey;
    private final UserRepository userRepository; // 사용자 정보를 저장하고 조회하기 위한 리포지토리
    private final PasswordEncoder passwordEncoder; // 비밀번호 암호화를 위한 인코더

    // 생성자: 비밀 키와 리포지토리, 인코더를 주입받음
    public AuthService(
            @Value("${service.jwt.secret-key}") String secretKey,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ){
        // Base64로 인코딩된 비밀 키를 디코드하여 HMAC SHA 키 생성
        this.secretKey = Keys.hmacShaKeyFor(
                Decoders.BASE64URL.decode(secretKey)
        );
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // 액세스 토큰을 생성하는 메서드
    public String createAccessToken(
            String userId
    ) {
        return Jwts.builder()
                .claim("user_id", userId) // JWT에 사용자 ID를 클레임으로 추가
                .issuer(issuer) // 발행자 정보 추가
                .issuedAt(new Date(System.currentTimeMillis())) // 발행 시간 추가
                .expiration(new Date(System.currentTimeMillis() + accessExpiration)) // 만료 시간 추가
                .signWith(secretKey, io.jsonwebtoken.SignatureAlgorithm.HS512) // 비밀 키로 서명
                .compact(); // JWT 생성
    }

    // 사용자 가입 처리 메서드
    public User signUp(
            User user
    ){
        // 입력된 비밀번호를 암호화하여 저장
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user); // 사용자 정보를 DB에 저장
    }

    // 사용자 로그인 처리 메서드
    public String signIn(
            String userId,
            String password
    ){
        // 사용자 ID로 사용자 조회
        User user = userRepository
                .findById(userId)
                .orElseThrow(
                        () -> new IllegalArgumentException("Invalid user ID or password") // 사용자 없음 예외 처리
                );
        // 입력된 비밀번호와 저장된 비밀번호 비교
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("Invalid user ID or password"); // 비밀번호 불일치 예외 처리
        }

        // 로그인 성공 시 액세스 토큰 생성하여 반환
        return createAccessToken(user.getUserId());
    }
}
