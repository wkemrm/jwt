package com.subject.jwt.service;

import com.subject.jwt.dto.JoinDto;
import com.subject.jwt.entity.Member;
import com.subject.jwt.repsitory.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Member joinUser(JoinDto joinDto) {
        joinDto.setPassword(passwordEncoder.encode(joinDto.getPassword()));
        Member member = memberRepository.save(Member.ofUser(joinDto));
        return member;
    }

    public Member joinAdmin(JoinDto joinDto) {
        joinDto.setPassword(passwordEncoder.encode(joinDto.getPassword()));
        Member member = memberRepository.save(Member.ofAdmin(joinDto));
        return member;
    }
}
