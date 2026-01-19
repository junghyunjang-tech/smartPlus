package com.zinidata.sample.domain.food;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 먹은 음식 기록 레포지토리
 * 
 * @author NICE ZiniData 개발팀
 */
@Repository
public interface FoodRecordRepository extends JpaRepository<FoodRecord, Long> {

    /**
     * 날짜별 음식 기록 조회
     * 
     * @param recordDate 조회할 날짜 (YYYYMMDD)
     * @return 해당 날짜의 음식 기록 리스트
     */
    List<FoodRecord> findByRecordDate(String recordDate);

    /**
     * 특정 사용자의 특정 날짜 음식 기록 조회
     * 
     * @param userId     유저 아이디
     * @param recordDate 조회할 날짜 (YYYYMMDD)
     * @return 해당 사용자의 해당 날짜 음식 기록 리스트
     */
    List<FoodRecord> findByUserIdAndRecordDate(String userId, String recordDate);
}
