package com.zinidata.sample.domain.food;

import com.zinidata.sample.domain.food.dto.FoodRecordResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 먹은 음식 기록 서비스
 * 
 * @author NICE ZiniData 개발팀
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class FoodRecordService {

    private final FoodRecordRepository foodRecordRepository;
    private final FoodNutritionRepository foodNutritionRepository;

    /**
     * 음식 기록 저장
     * 
     * @param userId   유저 아이디
     * @param foodId   음식 아이디
     * @param foodName 음식명
     * @return 저장된 음식 기록
     */
    @Transactional
    public FoodRecord saveFoodRecord(String userId, Long foodId, String foodName) {
        log.info("음식 기록 저장 시작: userId={}, foodId={}, foodName={}", userId, foodId, foodName);

        try {
            // 오늘 날짜 (YYYYMMDD)
            String recordDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

            // 음식 기록 생성
            FoodRecord foodRecord = FoodRecord.builder()
                    .recordDate(recordDate)
                    .userId(userId)
                    .foodId(foodId)
                    .foodList(foodName)
                    .build();

            FoodRecord saved = foodRecordRepository.save(foodRecord);
            log.info("음식 기록 저장 완료: recordId={}", saved.getRecordId());

            return saved;
        } catch (Exception e) {
            log.error("음식 기록 저장 실패: userId={}, foodId={}", userId, foodId, e);
            throw new RuntimeException("음식 기록 저장 중 오류가 발생했습니다.", e);
        }
    }

    /**
     * 특정 날짜의 음식 기록 조회
     * 
     * @param recordDate 조회할 날짜 (YYYYMMDD)
     * @return 음식 기록 리스트
     */
    @Transactional(readOnly = true)
    public List<FoodRecord> getFoodRecordsByDate(String recordDate) {
        log.info("음식 기록 조회: recordDate={}", recordDate);

        try {
            return foodRecordRepository.findByRecordDate(recordDate);
        } catch (Exception e) {
            log.error("음식 기록 조회 실패: recordDate={}", recordDate, e);
            throw new RuntimeException("음식 기록 조회 중 오류가 발생했습니다.", e);
        }
    }

    /**
     * 특정 사용자의 오늘 날짜 음식 기록 조회
     * 
     * @param userId 유저 아이디
     * @return 오늘의 음식 기록 리스트
     */
    @Transactional(readOnly = true)
    public List<FoodRecord> getTodayFoodRecords(String userId) {
        log.info("오늘의 음식 기록 조회: userId={}", userId);

        try {
            // 오늘 날짜 (YYYYMMDD)
            String today = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

            List<FoodRecord> records = foodRecordRepository.findByUserIdAndRecordDate(userId, today);
            log.info("오늘의 음식 기록 조회 완료: userId={}, count={}", userId, records.size());

            return records;
        } catch (Exception e) {
            log.error("오늘의 음식 기록 조회 실패: userId={}", userId, e);
            throw new RuntimeException("오늘의 음식 기록 조회 중 오류가 발생했습니다.", e);
        }
    }

    /**
     * 특정 사용자의 오늘 날짜 음식 기록 조회 (영양 정보 포함)
     * 
     * @param userId 유저 아이디
     * @return 오늘의 음식 기록 DTO 리스트 (영양 정보 포함)
     */
    @Transactional(readOnly = true)
    public List<FoodRecordResponseDto> getTodayFoodRecordsWithNutrition(String userId) {
        log.info("오늘의 음식 기록 조회 (영양 정보 포함): userId={}", userId);

        try {
            // 오늘 날짜 (YYYYMMDD)
            String today = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

            List<FoodRecord> records = foodRecordRepository.findByUserIdAndRecordDate(userId, today);
            log.info("오늘의 음식 기록 조회 완료: userId={}, count={}", userId, records.size());

            // FoodRecord를 DTO로 변환하면서 영양 정보 추가
            return records.stream()
                    .map(record -> {
                        FoodNutrition nutrition = foodNutritionRepository.findById(record.getFoodId())
                                .orElse(null);

                        return FoodRecordResponseDto.builder()
                                .recordId(record.getRecordId())
                                .foodId(record.getFoodId())
                                .foodName(record.getFoodList())
                                .calories(nutrition != null ? nutrition.getCalories() : BigDecimal.ZERO)
                                .protein(nutrition != null ? nutrition.getProtein() : BigDecimal.ZERO)
                                .build();
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("오늘의 음식 기록 조회 실패 (영양 정보 포함): userId={}", userId, e);
            throw new RuntimeException("오늘의 음식 기록 조회 중 오류가 발생했습니다.", e);
        }
    }

    /**
     * 음식 기록 삭제
     * 
     * @param recordId 기록 ID
     */
    @Transactional
    public void deleteFoodRecord(Long recordId) {
        log.info("음식 기록 삭제: recordId={}", recordId);

        try {
            foodRecordRepository.deleteById(recordId);
            log.info("음식 기록 삭제 완료: recordId={}", recordId);
        } catch (Exception e) {
            log.error("음식 기록 삭제 실패: recordId={}", recordId, e);
            throw new RuntimeException("음식 기록 삭제 중 오류가 발생했습니다.", e);
        }
    }
}
