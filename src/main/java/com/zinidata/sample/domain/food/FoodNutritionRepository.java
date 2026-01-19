package com.zinidata.sample.domain.food;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 식품 영양 정보 Repository
 * 
 * @author NICE ZiniData 개발팀
 */
@Repository
public interface FoodNutritionRepository extends JpaRepository<FoodNutrition, Long> {

    /**
     * 식품명으로 검색 (부분 일치)
     * 
     * @param keyword 검색 키워드
     * @return 검색된 식품 목록
     */
    List<FoodNutrition> findByFoodNameContaining(String keyword);
}
