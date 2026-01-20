package com.zinidata.sample.controller.api;

import com.zinidata.sample.dto.MemberInfoDto;
import com.zinidata.sample.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 회원 정보 API Controller
 * 
 * @author NICE ZiniData 개발팀
 * @since 1.0
 */
@Tag(name = "회원 정보", description = "회원 정보 조회 API")
@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
@Slf4j
public class MemberApiController {

    private final MemberService memberService;

    /**
     * 현재 로그인한 사용자의 정보 조회
     * 
     * @return 사용자 정보 (나이, 성별)
     */
    @Operation(summary = "사용자 정보 조회", description = "현재 로그인한 사용자의 나이와 성별 정보를 조회합니다.")
    @GetMapping("/info")
    public Map<String, Object> getMemberInfo() {
        log.info("사용자 정보 조회 요청");

        try {
            MemberInfoDto memberInfo = memberService.getCurrentMemberInfo();

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", memberInfo);

            return response;
        } catch (Exception e) {
            log.error("사용자 정보 조회 오류", e);

            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "사용자 정보 조회에 실패했습니다.");

            return response;
        }
    }
}
