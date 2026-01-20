package com.zinidata.sample.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zinidata.sample.config.GeminiConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Gemini API 호출 서비스
 * 
 * Google Gemini API를 호출하여 텍스트 생성 및 스트리밍 응답을 제공합니다.
 * 429 에러 발생 시 자동으로 재시도합니다.
 * 
 * @author NICE ZiniData 개발팀
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class GeminiService {

    private final GeminiConfig geminiConfig;
    private final WebClient.Builder webClientBuilder;
    private final ObjectMapper objectMapper = new ObjectMapper();

    /** 최대 재시도 횟수 */
    private static final int MAX_RETRY_ATTEMPTS = 3;

    /** 초기 재시도 대기 시간 (초) */
    private static final int INITIAL_BACKOFF_SECONDS = 2;

    /**
     * Gemini API를 호출하여 스트리밍 응답을 반환합니다.
     * 429 에러 발생 시 지수 백오프 방식으로 재시도합니다.
     * 
     * @param prompt 사용자 질문
     * @return 스트리밍 텍스트 응답
     */
    public Flux<String> generateContentStream(String prompt) {
        log.info("Gemini API 호출 시작: prompt={}", prompt);

        String url = String.format("%s/models/%s:streamGenerateContent?alt=sse&key=%s",
                geminiConfig.getBaseUrl(),
                geminiConfig.getModel(),
                geminiConfig.getKey());

        Map<String, Object> requestBody = buildRequestBody(prompt);

        log.debug("요청 URL: {}", url.replaceAll("key=.*", "key=***"));
        log.debug("요청 바디: {}", requestBody);

        WebClient webClient = webClientBuilder
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(10 * 1024 * 1024))
                .build();

        return webClient.post()
                .uri(url)
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToFlux(String.class)
                .doOnNext(response -> log.debug("원본 응답: {}", response))
                .map(this::extractTextFromResponse)
                .filter(text -> text != null && !text.isEmpty())
                .retryWhen(Retry.backoff(MAX_RETRY_ATTEMPTS, Duration.ofSeconds(INITIAL_BACKOFF_SECONDS))
                        .filter(this::isRetryableError)
                        .doBeforeRetry(
                                retrySignal -> log.warn("API 요청 재시도 중... 시도 횟수: {}", retrySignal.totalRetries() + 1))
                        .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> retrySignal.failure()))
                .doOnError(error -> log.error("Gemini API 호출 오류: {}", error.getMessage(), error))
                .onErrorResume(error -> {
                    if (error instanceof WebClientResponseException.TooManyRequests) {
                        return Flux.just("⚠️ API 요청 제한에 도달했습니다. 잠시 후 다시 시도해주세요.");
                    }
                    if (error instanceof WebClientResponseException) {
                        WebClientResponseException wcre = (WebClientResponseException) error;
                        log.error("API 에러 응답: {}", wcre.getResponseBodyAsString());
                    }
                    return Flux.just("오류가 발생했습니다: " + error.getMessage());
                });
    }

    /**
     * 재시도 가능한 에러인지 확인
     */
    private boolean isRetryableError(Throwable throwable) {
        return throwable instanceof WebClientResponseException.TooManyRequests;
    }

    /**
     * 요청 바디 생성
     * curl 테스트 성공 형식에 맞춤
     */
    private Map<String, Object> buildRequestBody(String prompt) {
        Map<String, Object> requestBody = new HashMap<>();

        // System Instruction (영양상담사 역할 설정)
        Map<String, Object> systemInstruction = new HashMap<>();
        Map<String, String> systemPart = new HashMap<>();
        systemPart.put("text", "너는 전문 영양상담사야. 사용자의 식단을 분석하고 " +
                "영양학적 관점에서 조언을 제공해. 친절하고 전문적으로 답변해줘.");
        systemInstruction.put("parts", List.of(systemPart));
        requestBody.put("system_instruction", systemInstruction);

        // Contents (사용자 입력)
        Map<String, Object> content = new HashMap<>();
        Map<String, String> part = new HashMap<>();
        part.put("text", prompt);
        content.put("parts", List.of(part));
        requestBody.put("contents", List.of(content));

        return requestBody;
    }

    /**
     * SSE 응답에서 텍스트 추출 (Jackson ObjectMapper 사용)
     */
    private String extractTextFromResponse(String response) {
        try {
            String jsonData = response;

            // SSE 형식: data: {"candidates":[...]}
            if (response.startsWith("data: ")) {
                jsonData = response.substring(6).trim();
            }

            // 빈 응답 또는 종료 신호 무시
            if (jsonData.isEmpty() || jsonData.equals("[DONE]")) {
                return "";
            }

            // Jackson으로 JSON 파싱
            JsonNode root = objectMapper.readTree(jsonData);

            // candidates[0].content.parts[0].text 경로에서 텍스트 추출
            JsonNode candidates = root.path("candidates");
            if (candidates.isArray() && candidates.size() > 0) {
                JsonNode content = candidates.get(0).path("content");
                JsonNode parts = content.path("parts");
                if (parts.isArray() && parts.size() > 0) {
                    JsonNode textNode = parts.get(0).path("text");
                    if (!textNode.isMissingNode()) {
                        return textNode.asText();
                    }
                }
            }

            return "";
        } catch (Exception e) {
            log.warn("응답 파싱 오류: {}", response, e);
            return "";
        }
    }
}
