package com.example.jelog.domain.user;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long userId;

    @Column(unique = true)
    private String userEmail;

    @Column
    private String userPw;

    @Column
    private String userIcon;

    @Builder
    public User(String userEmail, String userPw, String userIcon) {
        this.userEmail = userEmail;
        this.userPw = userPw;
        this.userIcon = userIcon;
    }
}
