package com.subject.jwt.entity;

import com.subject.jwt.dto.JoinDto;
import com.subject.jwt.enums.Role;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    public static Member ofUser(JoinDto joinDto) {
        return Member.builder()
                .username(joinDto.getUsername())
                .password(joinDto.getPassword())
                .role(Role.ROLE_USER)
                .build();
    }

    public static Member ofAdmin(JoinDto joinDto) {
        return Member.builder()
                .username(joinDto.getUsername())
                .password(joinDto.getPassword())
                .role(Role.ROLE_ADMIN)
                .build();
    }
}
