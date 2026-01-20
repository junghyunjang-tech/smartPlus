package com.zinidata.sample.controller.api;

import com.zinidata.sample.service.GeminiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

/**
 * Gemini API Controller
 * 
 * Gemini AI와의 채팅 기능을 제공하는 REST API
 * 
 * @author NICE ZiniData 개발팀
 * @since 1.0
 */
@Tag(name = "Gemini AI", description = "Gemini AI 채팅 API")
@RestController
@RequestMapping("/api/gemini")
@RequiredArgsConstructor
@Slf4j
public class GeminiApiController {

    private final GeminiService geminiService;

    /**
     * Gemini AI 채팅 (스트리밍)
     * 
     * Server-Sent Events(SSE)를 사용하여 실시간 스트리밍 응답을 제공합니다.
     * 
     * @param prompt 사용자 질문
     * @return 스트리밍 텍스트 응답
     */
    @Operation(summary = "AI 채팅 (스트리밍)", description = "Gemini AI와 대화하며 실시간으로 응답을 스트리밍합니다.")
    @GetMapping(value = "/chat/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> chatStream(
            @Parameter(description = "사용자 질문") @RequestParam String prompt) {

        log.info("AI 채팅 스트리밍 요청: prompt={}", prompt);

        return geminiService.generateContentStream(prompt);
    }
}
