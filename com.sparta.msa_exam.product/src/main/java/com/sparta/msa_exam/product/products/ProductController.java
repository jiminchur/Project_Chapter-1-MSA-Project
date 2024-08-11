package com.sparta.msa_exam.product.products;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // RESTful API 컨트롤러임을 나타내는 어노테이션
@RequestMapping("/products") // "/products" 경로로 들어오는 요청을 처리
@RequiredArgsConstructor // final 필드에 대한 생성자를 자동 생성
public class ProductController {

    private final ProductService productService; // 상품 서비스 주입

    @Value("${server.port}") // application.properties에서 서버 포트 값을 주입
    private String serverPort;

    // 상품 생성 엔드포인트
    @PostMapping
    public ProductDto createProduct(
            @RequestBody ProductDto productDto, // 요청 본문에서 상품 DTO를 가져옴
            HttpServletResponse response // HTTP 응답 객체
    ){
        addServerPortHeader(response); // 서버 포트 헤더 추가
        return productService.crateProduct(productDto); // 상품 생성 서비스 호출
    }

    // 모든 상품 조회 엔드포인트
    @GetMapping
    public List<ProductDto> readAllProduct(
            HttpServletResponse response // HTTP 응답 객체
    ){
        addServerPortHeader(response); // 서버 포트 헤더 추가
        return productService.readAllProduct(); // 모든 상품 조회 서비스 호출
    }

    // 특정 상품 조회 엔드포인트
    @GetMapping("{productId}") // 상품 ID를 경로 변수로 받음
    public ProductDto findProductById(
            @PathVariable("productId") Long productId, // 요청 경로에서 상품 ID 추출
            HttpServletResponse response // HTTP 응답 객체
    ){
        addServerPortHeader(response); // 서버 포트 헤더 추가
        return productService.findProductById(productId); // 상품 ID로 상품 조회 서비스 호출
    }

    // 서버 포트 정보를 응답 헤더에 추가하는 메서드
    private void addServerPortHeader(
            HttpServletResponse response // HTTP 응답 객체
    ) {
        response.addHeader(
                "Server-Port", // 헤더 이름
                serverPort); // 서버 포트 값 추가
    }
}
