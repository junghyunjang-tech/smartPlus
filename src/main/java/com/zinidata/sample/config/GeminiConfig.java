package com.zinidata.sample.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Gemini API 설정
 * 
 * application.yaml의 gemini.api 설정값을 바인딩합니다.
 * 
 * @author NICE ZiniData 개발팀
 * @since 1.0
 */
@Configuration
@ConfigurationProperties(prefix = "gemini.api")
@Getter
@Setter
public class GeminiConfig {

    /**
     * Gemini API 키
     */
    private String key;

    /**
     * API Base URL
     */
    private String baseUrl;

    /**
     * 사용할 모델명
     */
    private String model;

    /**
     * Thinking Level (LOW, MEDIUM, HIGH)
     */
    private String thinkingLevel;

    /**
     * WebClient.Builder 빈 생성
     * 
     * @return WebClient.Builder
     */
    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }
}
