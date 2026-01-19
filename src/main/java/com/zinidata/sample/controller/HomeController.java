package com.zinidata.sample.controller;

import com.zinidata.sample.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.ui.Model;
import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberRepository memberRepository;

    @GetMapping("/")
    public String home(Model model, Principal principal) {
        if (principal != null) {
            String memberId = principal.getName();
            model.addAttribute("memberId", memberId);

            // Get actual name from Member entity
            memberRepository.findByMemberId(memberId).ifPresent(member -> {
                model.addAttribute("userName", member.getName());
            });

            // Fallback if name not found
            if (!model.containsAttribute("userName")) {
                model.addAttribute("userName", memberId);
            }
        } else {
            model.addAttribute("userName", "Guest");
        }
        return "home";
    }
}
