package com.zinidata.sample.controller.api;

import com.zinidata.sample.common.dto.ApiResponse;
import com.zinidata.sample.domain.food.FoodNutrition;
import com.zinidata.sample.service.FoodNutritionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 식품 검색 API 컨트롤러
 * 
 * @author NICE ZiniData 개발팀
 */
@Tag(name = "식품 검색", description = "식품 영양 정보 검색 API")
@RestController
@RequestMapping("/api/food")
@RequiredArgsConstructor
public class FoodApiController {

    private final FoodNutritionService foodNutritionService;

    /**
     * 식품명으로 검색
     * 
     * @param keyword 검색 키워드
     * @return 검색 결과 목록
     */
    @Operation(summary = "식품 검색", description = "식품명으로 부분 일치 검색을 수행합니다")
    @GetMapping("/search")
    public ApiResponse<List<FoodNutrition>> searchFood(
            @Parameter(description = "검색 키워드") @RequestParam String keyword) {
        try {
            List<FoodNutrition> results = foodNutritionService.searchByFoodName(keyword);
            return ApiResponse.success(results);
        } catch (Exception e) {
            return ApiResponse.error(500, "검색 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
}
