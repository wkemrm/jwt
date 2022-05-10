package com.subject.jwt.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

@Getter
@Setter
public class Response<T> {
    private int status;
    private T content;

    public Response() {
        this.status = HttpStatus.OK.value();
    }

    public Response(T content) {
        this.status = HttpStatus.OK.value();
        this.content = content;
    }
}
