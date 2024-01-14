package com.example.jelog.repository;

import com.example.jelog.domain.post.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    @Query(value = "select * from post_like where post_id = :postId and user_id = :userId", nativeQuery = true)
    Optional<PostLike> findByPostIdAndUserId(Long postId, Long userId);
}
