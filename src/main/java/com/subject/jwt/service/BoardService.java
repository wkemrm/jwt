package com.subject.jwt.service;

import com.subject.jwt.dto.AddBoardDto;
import com.subject.jwt.dto.AddBoardResultDto;
import com.subject.jwt.entity.Board;
import com.subject.jwt.entity.Member;
import com.subject.jwt.repsitory.BoardRepository;
import com.subject.jwt.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    public AddBoardResultDto addBoard(AddBoardDto addBoardDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        Board board = Board.builder()
                .title(addBoardDto.getTitle())
                .content(addBoardDto.getContent())
                .createdDate(LocalDateTime.now())
                .member(Member.builder().id(principal.getId()).build())
                .build();
        Board save = boardRepository.save(board);

        return AddBoardResultDto.builder()
                .username(principal.getUsername())
                .title(save.getTitle())
                .content(save.getContent())
                .build();
    }
}
