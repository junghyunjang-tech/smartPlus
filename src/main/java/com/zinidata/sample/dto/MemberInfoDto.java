package com.zinidata.sample.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 사용자 정보 응답 DTO
 * 
 * AI 영양상담을 위한 사용자 기본 정보
 * 
 * @author NICE ZiniData 개발팀
 * @since 1.0
 */
@Getter
@Setter
@AllArgsConstructor
public class MemberInfoDto {

    /**
     * 나이
     */
    private Integer age;

    /**
     * 성별 (M: 남성, F: 여성)
     */
    private String gender;

    /**
     * 성별 한글명
     */
    private String genderName;
}
