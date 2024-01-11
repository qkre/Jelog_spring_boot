package com.example.jelog.repository;

import com.example.jelog.domain.post.Post;
import com.example.jelog.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    Optional<List<Post>> findAllByOrderByCreatedAtDesc();
    Optional<Post> findByUserAndPostId(User user, Long postId);
}
