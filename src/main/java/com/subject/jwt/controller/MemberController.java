package com.subject.jwt.controller;

import com.subject.jwt.dto.JoinDto;
import com.subject.jwt.dto.LoginDto;
import com.subject.jwt.dto.TokenDto;
import com.subject.jwt.service.MemberService;
import com.subject.jwt.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final JwtTokenUtil jwtTokenUtil;

    @PostMapping("/join/user")
    public String joinUser(@RequestBody JoinDto joinDto) {
        memberService.joinUser(joinDto);
        return "유저 회원가입 완료";
    }

    @PostMapping("/join/admin")
    public String joinAdmin(@RequestBody JoinDto joinDto) {
        memberService.joinAdmin(joinDto);
        return "어드민 회원가입 완료";
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody LoginDto loginDto) {
        return ResponseEntity.ok(memberService.login(loginDto));
    }

    @PostMapping("/logout")
    public String logout(@RequestHeader("Authorization") String accessToken, @RequestHeader("RefreshToken") String refreshToken) {
        System.out.println("accessToken = " + accessToken);
        System.out.println("refreshToken = " + refreshToken);
        memberService.logout(TokenDto.of(accessToken, refreshToken), jwtTokenUtil.getUsername(resolveToken(accessToken)));
        
        return "로그아웃 완료";
    }

    private String resolveToken(String token) {
        return token.substring(7);
    }
}
