package com.subject.jwt.controller;

import com.subject.jwt.dto.AddBoardDto;
import com.subject.jwt.dto.response.Response;
import com.subject.jwt.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;
    // 게시판 글 생성
    @PostMapping
    public Response addBoard(@RequestBody AddBoardDto addBoardDto) {

        return new Response(boardService.addBoard(addBoardDto));
    }

    // 모든 게시판 글 조회
    @GetMapping
    public Response getBoardList() {
        return new Response(boardService.getBoardList());
    }
    
    // 게시판 단일 조회
    @GetMapping("/{id}")
    public Response getBoard(@PathVariable Long id) {
        return new Response(boardService.getBoard(id));
    }
    
}
