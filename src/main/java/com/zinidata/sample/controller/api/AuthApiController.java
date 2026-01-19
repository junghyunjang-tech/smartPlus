package com.zinidata.sample.controller.api;

import com.zinidata.sample.common.dto.ApiResponse;
import com.zinidata.sample.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "인증 관리", description = "로그인/로그아웃 및 보안 관련 API")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthApiController {

    private final MemberService memberService;

    @Operation(summary = "아이디 중복 확인", description = "입력한 아이디의 사용 가능 여부를 확인합니다.")
    @GetMapping("/check-id")
    public ApiResponse<Boolean> checkIdDuplicate(
            @Parameter(description = "중복 확인할 아이디") @RequestParam String memberId) {
        boolean isDuplicate = memberService.checkIdDuplicate(memberId);
        // Duplicate means exists -> available is false.
        // But the requirement says "check duplicate", usually returning true if
        // available is better for frontend logic?
        // Or returning "isDuplicate" (true means bad).
        // Let's return !isDuplicate to mean "isAvailable"? Or just return the
        // duplication status.
        // Plan said: "Returns: ApiResponse<Boolean>".
        // Let's stick to true = duplicate exists, false = available (no duplicate).
        // Actually, usually check-id returns { available: true }.
        // Let's interpret the boolean as "isAvailable".

        // Wait, repository `existsByMemberId` returns true if exists (duplicate).
        // So `checkIdDuplicate` in service returning true means duplicate.

        // Let's return true if duplicate, false if available? Or maybe explicit
        // response.
        // Let's return isDuplicate directly.
        // Frontend will interpret: true -> duplicate (error), false -> available.
        return ApiResponse.success(isDuplicate);
    }
}
