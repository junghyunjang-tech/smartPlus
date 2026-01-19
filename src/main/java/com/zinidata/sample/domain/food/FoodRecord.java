package com.zinidata.sample.domain.food;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * 먹은 음식 기록 엔티티
 * 
 * @author NICE ZiniData 개발팀
 */
@Entity
@Table(name = "TB_FOOD_RECORD")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class FoodRecord {

    /**
     * 기록 ID (PK)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RECORD_ID")
    private Long recordId;

    /**
     * 날짜 (YYYYMMDD)
     */
    @Column(name = "RECORD_DATE", nullable = false, length = 8)
    private String recordDate;

    /**
     * 유저 아이디
     */
    @Column(name = "USER_ID", nullable = false, length = 20)
    private String userId;

    /**
     * 음식 아이디
     */
    @Column(name = "FOOD_ID")
    private Long foodId;

    /**
     * 음식 리스트 (JSON 형식으로 저장)
     */
    @Column(name = "FOOD_LIST", columnDefinition = "TEXT")
    private String foodList;

    /**
     * 등록일시
     */
    @CreatedDate
    @Column(name = "REG_DT", updatable = false)
    private LocalDateTime regDt;

    /**
     * 수정일시
     */
    @LastModifiedDate
    @Column(name = "CHG_DT")
    private LocalDateTime chgDt;

    @Builder
    public FoodRecord(String recordDate, String userId, Long foodId, String foodList) {
        this.recordDate = recordDate;
        this.userId = userId;
        this.foodId = foodId;
        this.foodList = foodList;
    }
}
