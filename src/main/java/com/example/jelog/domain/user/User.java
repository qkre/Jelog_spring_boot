package com.example.jelog.domain.user;

import com.example.jelog.domain.post.Post;
import com.example.jelog.domain.post.PostLike;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
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
    private String userNickName;

    @Column
    private String userPw;

    @Column
    private String userIcon;

    @Column
    private String role;


    @Builder
    public User(String userEmail, String userNickName, String userPw, String userIcon) {
        this.userEmail = userEmail;
        this.userNickName = userNickName;
        this.userPw = userPw;
        this.userIcon = userIcon;
    }
}
