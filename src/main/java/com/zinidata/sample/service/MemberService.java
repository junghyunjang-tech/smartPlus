package com.zinidata.sample.service;

import com.zinidata.sample.domain.member.Member;
import com.zinidata.sample.domain.member.MemberRepository;
import com.zinidata.sample.dto.MemberDto;
import com.zinidata.sample.dto.MemberInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void signup(MemberDto memberDto) {
        if (memberRepository.findByMemberId(memberDto.getMemberId()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }

        Member member = Member.builder()
                .memberId(memberDto.getMemberId())
                .password(passwordEncoder.encode(memberDto.getPassword()))
                .name(memberDto.getName())
                .birthDate(memberDto.getBirthDate())
                .gender(memberDto.getGender())
                .role("ROLE_USER")
                .useYn("Y")
                .build();

        memberRepository.save(member);
    }

    @Transactional(readOnly = true)
    public boolean checkIdDuplicate(String memberId) {
        return memberRepository.existsByMemberId(memberId);
    }

    /**
     * 현재 로그인한 사용자의 정보 조회
     * 
     * @return 사용자 정보 (나이, 성별)
     */
    @Transactional(readOnly = true)
    public MemberInfoDto getCurrentMemberInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String memberId = authentication.getName();

        Member member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Integer age = calculateAge(member.getBirthDate());
        String genderName = "M".equals(member.getGender()) ? "남성" : "여성";

        return new MemberInfoDto(age, member.getGender(), genderName);
    }

    /**
     * 생년월일로부터 나이 계산
     * 
     * @param birthDate 생년월일 (YYYYMMDD)
     * @return 만 나이
     */
    private Integer calculateAge(String birthDate) {
        if (birthDate == null || birthDate.length() != 8) {
            return null;
        }

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            LocalDate birth = LocalDate.parse(birthDate, formatter);
            LocalDate now = LocalDate.now();
            return Period.between(birth, now).getYears();
        } catch (Exception e) {
            return null;
        }
    }
}
