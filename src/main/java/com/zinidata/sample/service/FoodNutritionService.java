package com.zinidata.sample.service;

import com.zinidata.sample.domain.food.FoodNutrition;
import com.zinidata.sample.domain.food.FoodNutritionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 식품 영양 정보 서비스
 * 
 * @author NICE ZiniData 개발팀
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class FoodNutritionService {

    private final FoodNutritionRepository foodNutritionRepository;

    /**
     * 식품명으로 검색
     * 
     * @param keyword 검색 키워드
     * @return 검색된 식품 목록
     */
    public List<FoodNutrition> searchByFoodName(String keyword) {
        log.info("식품 검색 시작: keyword={}", keyword);

        if (keyword == null || keyword.trim().isEmpty()) {
            log.warn("검색 키워드가 비어있습니다");
            return List.of();
        }

        List<FoodNutrition> results = foodNutritionRepository.findByFoodNameContaining(keyword.trim());
        log.info("식품 검색 완료: keyword={}, count={}", keyword, results.size());

        return results;
    }
}
