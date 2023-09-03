package com.foocmend.controllers.member;

import com.foocmend.commons.MemberUtil;
import com.foocmend.entities.Member;
import com.foocmend.repositories.MemberRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member/withdraw")
public class Withdraw {

    private final MemberRepository repository;
    private final MemberUtil memberUtil;
    private final HttpSession session;

    @GetMapping
    public String withdraw() {


        return "front/member/withdraw";
    }

    @PostMapping
    public String withdrawPs(@RequestParam String email, @RequestParam String password) {

        if(!memberUtil.isLogin()) {
            return "redirect:/member/login";
        }

        Member currMember = memberUtil.getEntity();
        Member member = repository.findByEmail(email);

        if(currMember != null && currMember.getEmail().equals(email)/** && currMember.getPassword().equals(password)*/) {

            repository.delete(member);
            repository.flush();
            session.invalidate();

            return "redirect:/";
        } else {
            return "redirect:/member/withdraw";
        }
    }


    private boolean passwordMatches(String encodedPassword, String password) {

         PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
         passwordEncoder.matches(password, encodedPassword);
        return encodedPassword.equals(password);
    }


}
