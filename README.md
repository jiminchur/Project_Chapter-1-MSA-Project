# Project_Chapter-1-MSA-Project
> MSA 아키텍처를 구성(Eureka,Ribbon,Redis,API Gateway,Oauth2,JWT,...)하여 요구사항에 맞춘 프로젝트를 진행 하였음

## 개발 기간
* 2024.08.07 ~ 2024.08.11

## 목차
[0. 파일구성](#0-파일구성)

[1. 필수기능 - 기본 API 구성하기](#1-필수기능---기본-api-구성하기)

[2. 상품서비스 라운드 로빈 형식으로 로드밸런싱 구성](#2-상품서비스-라운드-로빈-형식으로-로드밸런싱-구성)

[3. 주문에 상품을 추가하는 API 만들때 아래와 같은 구성](#3-주문에-상품을-추가하는-api-만들때-아래와-같은-구성)

[4. 분산 추적 구현해보기](#4-분산-추적-구현해보기)

[5. 캐싱 기능 구현하기](#5-캐싱-기능-구현하기)

[6. 로그인 기능과 회원가입기능 구현하기](#6-로그인-기능과-회원가입기능-구현하기)

[7. 외부 요청 보호](#7-외부-요청-보호)

[8. 로컬과 서버의 환경을 분리하기](#8-로컬과-서버의-환경을-분리하기)

## 0. 파일구성
* 유레카 서버
    * `com.sparta.msa_exam.server` 
    * `19090`

* 게이트웨이 서비스
    * `com.sparta.msa_exam.gateway` 
    * `19091` 

* 상품 서비스
    * `com.sparta.msa_exam.product` 
    * `19093`,`19094`

* 주문 서비스
    * `com.sparta.msa_exam.order` 
    * `19092`

* 인증 서비스
    * `com.sparta.msa_exam.auth` 
    * `19095`

* Eureka 등록
![eureka](/readme-img/Eureka.png)
## 1. 필수기능 - 기본 API 구성하기
* POST /products  상품 추가 API
![POST /products](/readme-img/1-1.png)
* GET /products 상품 목록 조회 API
![GET /products](/readme-img/1-2.png)
* POST /order 주문 추가 API
![POST /order](/readme-img/1-3.png)
* PUT /order/{orderId}  주문에 상품을 추가하는 API
![PUT /order/{orderId}](/readme-img/1-5.png)
* GET /order/{orderId}  주문 단건 조회 API
![GET /order/{orderId}](/readme-img/1-4.png)
## 2. 상품서비스 라운드 로빈 형식으로 로드밸런싱 구성
* [Product] 복제하여 19094포트 생성하기
![19094 생성한 img](/readme-img/2-2.png)
* 상품 목록 조회 했을때 19093포트일때
![19093](/readme-img/1-2.png)
* 상품 목록 조회 했을때 19094포트일때
![19094](/readme-img/2-1.png)
## 3. 주문에 상품을 추가하는 API 만들때 아래와 같은 구성
1. FeignClient를 이용해서 주문 서비스에 상품서비스 클라이언트 연결
2. 상품 목록 조회 API를 호출해서 파라미터로 받은 product_id가 상품 목록에 존재하는지 검증
3. 존재할 경우 주문에 상품을 추가하고, 존재하지 않는다면 주문에 저장하지 않음
![존재하지 않은 상품 조회 - 오류](/readme-img/3-1.png)
> 사용자가 구매할때 상품이 존재하지 않으면 오류가 나는 로직을 구현하였다. 현재 productid는 1과 2가 있으며 3은 존재하지 않을때 3을 구매한다고 하면 오류가 난 이미지 이다.
## 4. 분산 추적 구현해보기
* http://localhost:9411/zipkin/ 에서 RunQuery를 날렸을때 
![zipkin img](/readme-img/4-1.png)
## 5. 캐싱 기능 구현하기
* 조회 했을때
![조회 캐싱](/readme-img/조회-cache.png)
* 상품을 등록했을때
![등록 캐싱 및 조회캐싱 사라지는거](/readme-img/create-cache.png)
> 요구사항에서 상품조회 API의 결과를 60초 동안 메모리에 캐싱된 데이터 보여주기와 만약 상품추가 API가 호출될 경우 상품조회 API 응답 데이터 캐시가 갱신되도록 구현 하였다.
## 6. 로그인 기능과 회원가입기능 구현하기
* SingUp
![signUp](/readme-img/6-signup.png)
* SingIn
![signIn](/readme-img/6-signin.png)
## 7. 외부 요청 보호
* 상품서비스 - 401
![product - 401](/readme-img/7-product-401.png)
* 주문서비스 - 401
![order - 401](/readme-img/7-order-401.png)
> Oauth2,JWT 기반으로 인증/인가를 구성하여 인가 없이 상품 서비스, 주문 서비스를 호출할 때 401 HTTP Status Code를 응답하도록 설정하였다.
## 8. 로컬과 서버의 환경을 분리하기
![dev-prod 분리하기](/readme-img/dev-prod.png)
> 로컬에서는 localhost:3306 으로 DB에 접근하고, 서버에 배포시엔 RDS 주소로 접근하게 구성하도록 환경을 분리하였다.
