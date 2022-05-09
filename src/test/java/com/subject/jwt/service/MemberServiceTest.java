package com.subject.jwt.service;

import com.subject.jwt.dto.JoinDto;
import com.subject.jwt.entity.Member;
import com.subject.jwt.enums.Role;
import com.subject.jwt.repsitory.MemberRepository;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

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
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @InjectMocks
    MemberService memberService;

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
}