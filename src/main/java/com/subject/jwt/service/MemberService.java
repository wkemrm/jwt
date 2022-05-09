package com.subject.jwt.service;

import com.subject.jwt.dto.JoinDto;
import com.subject.jwt.dto.LoginDto;
import com.subject.jwt.dto.TokenDto;
import com.subject.jwt.entity.Member;
import com.subject.jwt.enums.JwtExpirationEnum;
import com.subject.jwt.redis.CacheKey;
import com.subject.jwt.redis.LogoutAccessToken;
import com.subject.jwt.redis.RefreshToken;
import com.subject.jwt.redis.repository.LogoutAccessTokenRedisRepository;
import com.subject.jwt.redis.repository.RefreshTokenRedisRepository;
import com.subject.jwt.repsitory.MemberRepository;
import com.subject.jwt.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final RefreshTokenRedisRepository refreshTokenRedisRepository;
    private final LogoutAccessTokenRedisRepository logoutAccessTokenRedisRepository;

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

    public TokenDto login(LoginDto loginDto) {
        Member member = memberRepository.findByUsername(loginDto.getUsername()).orElseThrow(() -> new NoSuchElementException("회원이 없습니다."));
        checkPassword(loginDto.getPassword(), member.getPassword());

        String accessToken = jwtTokenUtil.generateAccessToken(loginDto.getUsername());
        RefreshToken refreshToken = saveRefreshToken(loginDto.getUsername());

        return TokenDto.of(accessToken, refreshToken.getRefreshToken());
    }

    private void checkPassword(String password, String findPassword) {
        if (!passwordEncoder.matches(password, findPassword)) {
            throw new NoSuchElementException("비밀번호가 맞지 않습니다.");
        }
    }

    private RefreshToken saveRefreshToken(String username) {
        return refreshTokenRedisRepository.save(RefreshToken.createRefreshToken(username,
                jwtTokenUtil.generateRefreshToken(username), JwtExpirationEnum.REFRESH_TOKEN_EXPIRATION_TIME.getValue()
                ));
    }

    @CacheEvict(value = CacheKey.USER, key = "#username")
    public void logout(TokenDto tokenDto, String username) {
        String accessToken = resolveToken(tokenDto.getAccessToken());
        long remainMilliSeconds = jwtTokenUtil.getRemainMilliSeconds(accessToken);
        refreshTokenRedisRepository.deleteById(username);
        logoutAccessTokenRedisRepository.save(LogoutAccessToken.of(accessToken, username, remainMilliSeconds));
    }

    private String resolveToken(String token) {
        return token.substring(7);
    }
}
