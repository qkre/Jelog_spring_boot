package com.example.jelog.domain.post;

import com.example.jelog.domain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table
@Entity
public class PostLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long postLikeId;

    @Column
    private Long userId;

    @Builder
    public PostLike(Long userId) {
        this.userId = userId;
    }
}
