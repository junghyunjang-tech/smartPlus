package com.zinidata.sample.service;

import com.zinidata.sample.domain.member.Member;
import com.zinidata.sample.domain.member.MemberRepository;
import com.zinidata.sample.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
                .role("ROLE_USER")
                .useYn("Y")
                .build();

        memberRepository.save(member);
    }

    @Transactional(readOnly = true)
    public boolean checkIdDuplicate(String memberId) {
        return memberRepository.existsByMemberId(memberId);
    }
}
