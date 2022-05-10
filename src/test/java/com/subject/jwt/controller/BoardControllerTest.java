package com.subject.jwt.controller;

import com.google.gson.Gson;
import com.subject.jwt.config.SecurityConfig;
import com.subject.jwt.dto.AddBoardDto;
import com.subject.jwt.dto.AddBoardResultDto;
import com.subject.jwt.dto.BoardDto;
import com.subject.jwt.dto.UpdateBoardDto;
import com.subject.jwt.filter.JwtAuthenticationFilter;
import com.subject.jwt.service.BoardService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = BoardController.class,
    excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)
,@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class)})
@AutoConfigureWebMvc
class BoardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BoardService boardService;

    @Test
    @DisplayName("새 게시글 추가 테스트")
    @WithMockUser("USER")
    void addBoard() throws Exception {
        given(boardService.addBoard(any(AddBoardDto.class)))
                .willReturn(new AddBoardResultDto("username", "title", "content"));

        AddBoardDto addBoardDto = new AddBoardDto("title", "content");
        Gson gson = new Gson();
        String content = gson.toJson(addBoardDto);

        mockMvc.perform(
                post("/board")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.status").exists())
                .andExpect(jsonPath("$.content.username").exists())
                .andExpect(jsonPath("$.content.title").exists())
                .andExpect(jsonPath("$.content.content").exists())
                .andDo(print());

        verify(boardService).addBoard(any(AddBoardDto.class));
    }

    @Test
    @WithMockUser("USER")
    void getBoardList() throws Exception {
        List result = new ArrayList();
        result.add(new BoardDto("title", "content", LocalDateTime.of(2022, 10, 2, 3, 3), "username"));
        given(boardService.getBoardList())
                .willReturn(result);

        mockMvc.perform(
                get("/board"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").exists())
                .andExpect(jsonPath("$.content[*].title").exists())
                .andExpect(jsonPath("$.content[*].content").exists())
                .andExpect(jsonPath("$.content[*].createdDate").exists())
                .andExpect(jsonPath("$.content[*].username").exists())
                .andDo(print());

        verify(boardService).getBoardList();
    }

    @Test
    @WithMockUser("USER")
    void getBoard() throws Exception {
        Long id = 1l;
        given(boardService.getBoard(id))
                .willReturn(new BoardDto("title", "content", LocalDateTime.of(2022, 10, 2, 3, 3), "username"));

        mockMvc.perform(
                get("/board/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").exists())
                .andExpect(jsonPath("$.content.title").exists())
                .andExpect(jsonPath("$.content.createdDate").exists())
                .andExpect(jsonPath("$.content.content").exists())
                .andExpect(jsonPath("$.content.username").exists())
                .andDo(print());

        verify(boardService).getBoard(id);
    }

    @Test
    @WithMockUser("USER")
    void updateBoard() throws Exception {
        Long id = 1l;
        Map<String, Long> result = new HashMap<>();
        result.put("result", 1l);
        UpdateBoardDto updateBoardDto = new UpdateBoardDto("title", "content");

        Gson gson = new Gson();
        String content = gson.toJson(updateBoardDto);
        given(boardService.updateBoard(any(), any(UpdateBoardDto.class)))
                .willReturn(result);

        mockMvc.perform(
                put("/board/" + id)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").exists())
                .andExpect(jsonPath("$.content.result").exists())
                .andDo(print());

        verify(boardService).updateBoard(any(), any(UpdateBoardDto.class));
    }

    @Test
    @WithMockUser("USER")
    void deleteBoard() throws Exception {
        Map<String, Long> result = new HashMap<>();
        result.put("result", 1l);
        given(boardService.deleteBoard(any())).willReturn(result);

        mockMvc.perform(
                        delete("/board/" + 1l)
                                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").exists())
                .andExpect(jsonPath("$.content.result").exists())
                .andDo(print());

        verify(boardService).deleteBoard(any());
    }
}