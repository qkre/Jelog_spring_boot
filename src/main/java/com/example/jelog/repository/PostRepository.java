package com.example.jelog.repository;

import com.example.jelog.domain.post.Post;
import com.example.jelog.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findAll(Pageable pageable);
    Optional<Page<Post>> findAllByOrderByCreatedAtDesc(Pageable pageable);
    Optional<List<Post>> findByUser(User user);



    Optional<List<Post>> findByUserOrderByCreatedAtDesc(User user);
    Optional<List<Post>> findByUserOrderByCreatedAtAsc(User user);

    Optional<Integer> countByUser(User user);
    Optional<Post> findByUserAndPostId(User user, Long postId);


}
