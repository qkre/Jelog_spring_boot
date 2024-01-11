package com.example.jelog.domain.post;

import com.example.jelog.domain.user.User;
import com.example.jelog.utils.StringListConverter;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Getter
@Table
@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long postId;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    @CreatedDate
    private LocalDateTime createdAt;

    @Column
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Column
    private String title;

    @Column
    private String content;

    @Column
    @Convert(converter = StringListConverter.class)
    private List<String> tags;

    @Builder
    public Post(User user, String title, String content, List<String> tags) {
        this.user = user;
        this.title = title;
        this.content = content;
        this.tags = tags;
    }

    public void update(String title, String content, List<String> tags){
        this.title = title;
        this.content = content;
        this.tags = tags;
    }
}
