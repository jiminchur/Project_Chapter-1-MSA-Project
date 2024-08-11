package com.sparta.msa_exam.product.products;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.CacheKeyPrefix;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.time.Duration;
import static org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;

@Configuration // 설정 클래스임을 나타냄
@EnableCaching // 캐싱 기능을 활성화
public class CacheConfig {

    // CacheManager 빈을 정의하는 메서드
    @Bean
    public CacheManager cacheManager(
            RedisConnectionFactory redisConnectionFactory // Redis 연결 팩토리 주입
    ) {
        // Redis 캐시 설정 생성
        RedisCacheConfiguration configuration = RedisCacheConfiguration
                .defaultCacheConfig() // 기본 캐시 설정 가져오기
                .disableCachingNullValues() // null 값을 캐시하지 않도록 설정
                .entryTtl(Duration.ofSeconds(60)) // 캐시 항목의 TTL(Time To Live)을 60초로 설정
                .computePrefixWith(CacheKeyPrefix.simple()) // 캐시 키 프리픽스 설정
                .serializeValuesWith(
                        SerializationPair.fromSerializer(RedisSerializer.java()) // 값 직렬화 방식 설정
                );

        // RedisCacheManager를 생성하고 반환
        return RedisCacheManager
                .builder(redisConnectionFactory) // Redis 연결 팩토리로 빌더 생성
                .cacheDefaults(configuration) // 기본 캐시 설정 적용
                .build(); // 캐시 매니저 객체 빌드 및 반환
    }
}