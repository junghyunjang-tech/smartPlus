package com.zinidata.sample.domain.food.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 음식 기록 응답 DTO
 * 
 * @author NICE ZiniData 개발팀
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FoodRecordResponseDto {

    /**
     * 기록 ID
     */
    private Long recordId;

    /**
     * 음식 ID
     */
    private Long foodId;

    /**
     * 음식명
     */
    private String foodName;

    /**
     * 열량 (kcal)
     */
    private BigDecimal calories;

    /**
     * 단백질 (g)
     */
    private BigDecimal protein;
}
