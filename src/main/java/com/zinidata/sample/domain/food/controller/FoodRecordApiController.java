package com.zinidata.sample.domain.food.controller;

import com.zinidata.sample.domain.food.FoodRecord;
import com.zinidata.sample.domain.food.FoodRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 먹은 음식 기록 API 컨트롤러
 * 
 * @author NICE ZiniData 개발팀
 */
@Tag(name = "음식 기록 관리", description = "먹은 음식 기록 추가/조회/삭제 API")
@RestController
@RequestMapping("/api/food/record")
@RequiredArgsConstructor
@Slf4j
public class FoodRecordApiController {

    private final FoodRecordService foodRecordService;

    /**
     * 음식 기록 추가
     * 
     * @param foodId         음식 아이디
     * @param foodName       음식명
     * @param authentication 인증 정보
     * @return 응답 데이터
     */
    @Operation(summary = "음식 기록 추가", description = "검색한 음식을 오늘 먹은 음식으로 추가합니다")
    @PostMapping("/add")
    public Map<String, Object> addFoodRecord(
            @Parameter(description = "음식 아이디") @RequestParam Long foodId,
            @Parameter(description = "음식명") @RequestParam String foodName,
            Authentication authentication) {

        Map<String, Object> response = new HashMap<>();

        try {
            // 로그인한 사용자 ID 가져오기
            String userId = authentication.getName();

            log.info("음식 기록 추가 요청: userId={}, foodId={}, foodName={}", userId, foodId, foodName);

            // 음식 기록 저장
            FoodRecord saved = foodRecordService.saveFoodRecord(userId, foodId, foodName);

            response.put("success", true);
            response.put("message", "음식 기록이 추가되었습니다.");
            response.put("data", saved);

        } catch (Exception e) {
            log.error("음식 기록 추가 실패", e);
            response.put("success", false);
            response.put("message", "음식 기록 추가 중 오류가 발생했습니다.");
            response.put("data", null);
        }

        return response;
    }

    /**
     * 오늘의 음식 기록 조회
     * 
     * @param authentication 인증 정보
     * @return 응답 데이터
     */
    @Operation(summary = "오늘의 음식 기록 조회", description = "로그인한 사용자의 오늘 먹은 음식 목록을 조회합니다")
    @GetMapping("/today")
    public Map<String, Object> getTodayFoodRecords(Authentication authentication) {

        Map<String, Object> response = new HashMap<>();

        try {
            // 로그인한 사용자 ID 가져오기
            String userId = authentication.getName();

            log.info("오늘의 음식 기록 조회 요청: userId={}", userId);

            // 오늘의 음식 기록 조회 (영양 정보 포함)
            var records = foodRecordService.getTodayFoodRecordsWithNutrition(userId);

            response.put("success", true);
            response.put("message", "조회 성공");
            response.put("data", records);

        } catch (Exception e) {
            log.error("오늘의 음식 기록 조회 실패", e);
            response.put("success", false);
            response.put("message", "조회 중 오류가 발생했습니다.");
            response.put("data", null);
        }

        return response;
    }

    /**
     * 음식 기록 삭제
     * 
     * @param recordId 기록 ID
     * @return 응답 데이터
     */
    @Operation(summary = "음식 기록 삭제", description = "추가한 음식 기록을 삭제합니다")
    @DeleteMapping("/{recordId}")
    public Map<String, Object> deleteFoodRecord(
            @Parameter(description = "기록 ID") @PathVariable Long recordId) {

        Map<String, Object> response = new HashMap<>();

        try {
            log.info("음식 기록 삭제 요청: recordId={}", recordId);

            foodRecordService.deleteFoodRecord(recordId);

            response.put("success", true);
            response.put("message", "음식 기록이 삭제되었습니다.");
            response.put("data", null);

        } catch (Exception e) {
            log.error("음식 기록 삭제 실패", e);
            response.put("success", false);
            response.put("message", "음식 기록 삭제 중 오류가 발생했습니다.");
            response.put("data", null);
        }

        return response;
    }
}
