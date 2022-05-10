package com.subject.jwt.entity;

import com.subject.jwt.dto.JoinDto;
import com.subject.jwt.enums.Role;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(name = "USERNAME", unique = true)
    private String username;

    @Column(name = "PASSWORD")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "ROLE")
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
