package com.zinidata.sample.domain.food;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * 식품 영양 정보 엔티티
 * 
 * @author NICE ZiniData 개발팀
 */
@Entity
@Table(name = "TB_FOOD_NUTRITION")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class FoodNutrition {

    /**
     * 식품 ID (PK)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FOOD_ID")
    private Long foodId;

    /**
     * 식품명
     */
    @Column(name = "FOOD_NAME", nullable = false, length = 200)
    private String foodName;

    /**
     * 1회 제공량 (g)
     */
    @Column(name = "SERVING_SIZE")
    private BigDecimal servingSize;

    /**
     * 열량 (kcal)
     */
    @Column(name = "CALORIES")
    private BigDecimal calories;

    /**
     * 탄수화물 (g)
     */
    @Column(name = "CARBOHYDRATE")
    private BigDecimal carbohydrate;

    /**
     * 단백질 (g)
     */
    @Column(name = "PROTEIN")
    private BigDecimal protein;

    /**
     * 지방 (g)
     */
    @Column(name = "FAT")
    private BigDecimal fat;

    /**
     * 나트륨 (mg)
     */
    @Column(name = "SODIUM")
    private BigDecimal sodium;
}
