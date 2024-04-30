package com.example.jelog.domain.post;

import com.example.jelog.domain.comment.Comment;
import com.example.jelog.domain.user.User;
import com.example.jelog.utils.StringListConverter;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
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

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "post_id")
    private List<Comment> comments;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "post_id")
    private Set<PostLike> postLikes;

    @Column
    @CreatedDate
    private LocalDateTime createdAt;

    @Column
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Column
    private String title;

    @Column(length = 100000)
    private String content;

    @Column
    private String thumbnail;

    @Column
    @Convert(converter = StringListConverter.class)
    private List<String> tags;

    @Builder
    public Post(User user, String title, String thumbnail, String content, List<String> tags) {
        this.user = user;
        this.title = title;
        this.thumbnail = thumbnail;
        this.content = content;
        this.tags = tags;
    }

    public void update(String title, String content, String thumbnail, List<String> tags){
        this.title = title;
        this.content = content;
        this.thumbnail = thumbnail;
        this.tags = tags;
    }
}
