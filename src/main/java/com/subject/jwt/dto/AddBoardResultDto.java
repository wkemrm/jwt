package com.subject.jwt.dto;

import com.subject.jwt.entity.Board;
import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddBoardResultDto {
    private String username;
    private String title;
    private String content;
}
