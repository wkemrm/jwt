package com.subject.jwt.service;

import com.subject.jwt.dto.JoinDto;
import com.subject.jwt.dto.LoginDto;
import com.subject.jwt.dto.TokenDto;
import com.subject.jwt.entity.Member;
import com.subject.jwt.enums.JwtExpirationEnum;
import com.subject.jwt.enums.Role;
import com.subject.jwt.redis.RefreshToken;
import com.subject.jwt.redis.repository.RefreshTokenRedisRepository;
import com.subject.jwt.repsitory.MemberRepository;


import com.subject.jwt.util.JwtTokenUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {
    @Mock
    MemberRepository memberRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    MemberService memberService;

    @Mock
    JwtTokenUtil jwtTokenUtil;

    @Mock
    RefreshTokenRedisRepository refreshTokenRedisRepository;
    @Test
    void joinUser() {
        //given
        Member member = Member.builder()
                .username("yhn8")
                .password("1234")
                .role(Role.ROLE_USER)
                .build();

        when(memberRepository.save(any())).thenReturn(member);

        //when
        Member result = memberService.joinUser(new JoinDto("yhn8", "1234"));

        //then
        verify(memberRepository, times(1)).save(any());
        assertThat(result, equalTo(member));
    }

    @Test
    void joinAdmin() {
        //given
        Member member = Member.builder()
                .username("yhn8")
                .password("1234")
                .role(Role.ROLE_ADMIN)
                .build();
        when(memberRepository.save(any())).thenReturn(member);

        //when
        Member result = memberService.joinAdmin(new JoinDto("yhn8", "1234"));

        //then
        verify(memberRepository, times(1)).save(any());
        assertThat(result, equalTo(member));
    }

    @Test
    void login() {
        //given
        Member member = Member.builder()
                .username("yhn8")
                .password("1234")
                .role(Role.ROLE_ADMIN)
                .build();
        Optional<Member> optionalMember = Optional.of(member);

        when(memberRepository.findByUsername(any())).thenReturn(optionalMember);
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(jwtTokenUtil.generateAccessToken(any())).thenReturn("accessToken");
        when(jwtTokenUtil.generateRefreshToken(any())).thenReturn("refreshToken");
        RefreshToken refreshToken = RefreshToken.createRefreshToken("yhn8",
                jwtTokenUtil.generateRefreshToken("yhn8"), JwtExpirationEnum.REFRESH_TOKEN_EXPIRATION_TIME.getValue()
        );

        when(refreshTokenRedisRepository.save(any())).thenReturn(refreshToken);

        //when
        TokenDto tokenDto = memberService.login(new LoginDto("yhn8", "1234"));

        //then
        verify(memberRepository, times(1)).findByUsername(any());
        verify(jwtTokenUtil, times(1)).generateAccessToken(any());
        verify(refreshTokenRedisRepository, times(1)).save(any());

        assertThat("accessToken", equalTo(tokenDto.getAccessToken()));
        assertThat("refreshToken", equalTo(tokenDto.getRefreshToken()));
    }

    @Test
    public void test() {
        System.out.println("passwordEncoder.encode(\"1234\") = " + passwordEncoder.encode("1234"));
        System.out.println("passwordEncoder.encode(\"1234\") = " + passwordEncoder.encode("1234"));
    }
}