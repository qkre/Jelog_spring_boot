package com.example.jelog.service.post;

import com.example.jelog.domain.post.Post;
import com.example.jelog.domain.post.PostLike;
import com.example.jelog.domain.user.User;
import com.example.jelog.exception.AppException;
import com.example.jelog.exception.ErrorCode;
import com.example.jelog.jwt.JwtUtil;
import com.example.jelog.repository.PostLikeRepository;
import com.example.jelog.repository.PostRepository;
import com.example.jelog.repository.UserRepository;
import com.example.jelog.web.dto.post.AddPostRequestDto;
import com.example.jelog.web.dto.post.DeletePostRequestDto;
import com.example.jelog.web.dto.post.LikePostRequestDto;
import com.example.jelog.web.dto.post.UnlikePostRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.*;

@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;

    @Value("${jwt.secret}")
    private String secretKey;

    // C
    public boolean write(AddPostRequestDto requestDto, User user) {
        if (!JwtUtil.isTokenOwner(requestDto.getToken(), secretKey, requestDto.getUserEmail())) {
            throw new AppException(ErrorCode.WRONG_ACCEPT);
        }

        postRepository.save(
                Post.builder()
                        .user(user)
                        .title(requestDto.getTitle())
                        .content(requestDto.getContent())
                        .thumbnail(requestDto.getThumbnail())
                        .tags(requestDto.getTags())
                        .build()
        );

        return true;
    }

    public boolean likePost(LikePostRequestDto requestDto) {
        if (!JwtUtil.isTokenOwner(requestDto.getToken(), secretKey, requestDto.getUserEmail())) {
            throw new AppException(ErrorCode.WRONG_ACCEPT);
        }

        Post post = postRepository.findById(requestDto.getPostId()).orElseThrow(() -> new AppException(ErrorCode.POSTS_DONT_EXIST));

        Set<PostLike> postLikes = post.getPostLikes();

        boolean alreadyLikedUser = postLikes.stream().anyMatch(postLike -> postLike.getUserEmail().equals(requestDto.getUserEmail()));

        if (!alreadyLikedUser) {
            postLikes.add(PostLike.builder().userEmail(requestDto.getUserEmail()).build());
            postRepository.save(post);

            return true;
        }

        return false;
    }

    public boolean unLikePost(UnlikePostRequestDto requestDto) {
        if (!JwtUtil.isTokenOwner(requestDto.getToken(), secretKey, requestDto.getUserEmail())) {
            throw new AppException(ErrorCode.WRONG_ACCEPT);
        }

        Post post = postRepository.findById(requestDto.getPostId()).orElseThrow(() -> new AppException(ErrorCode.POSTS_DONT_EXIST));

        Set<PostLike> postLikes = post.getPostLikes();

        boolean alreadyLikedUser = postLikes.stream().anyMatch(postLike -> postLike.getUserEmail().equals(requestDto.getUserEmail()));

        if (alreadyLikedUser) {
            PostLike target = postLikeRepository.findByPostIdAndUserEmail(requestDto.getPostId(), requestDto.getUserEmail()).orElseThrow(() -> new AppException(ErrorCode.WRONG_ACCEPT));

            postLikes.remove(target);

            postRepository.save(post);
            return true;
        }

        return false;
    }

    // R
    public Page<Post> findAllOrderByCreatedAtDesc(Pageable pageable) {
        Page<Post> posts = postRepository.findAllByOrderByCreatedAtDesc(pageable).orElseThrow(
                () -> new AppException(ErrorCode.POSTS_DONT_EXIST));
        posts.forEach(post -> post.getUser().setUserPw(null));
        return posts;
    }

    public Page<Post> findAll(Pageable pageable) {
        Page<Post> posts = postRepository.findAll(pageable);

        return posts;
    }

    public List<Post> findByUserOrderByCreatedAt(User user, String orderBy) {
        List<Post> posts = null;
        if (orderBy.equals("desc")) {
            posts = postRepository.findByUserOrderByCreatedAtDesc(user).orElseThrow(
                    () -> new AppException(ErrorCode.POSTS_DONT_EXIST)
            );
        }
        if (orderBy.equals("asc")) {
            posts = postRepository.findByUserOrderByCreatedAtAsc(user).orElseThrow(
                    () -> new AppException(ErrorCode.POSTS_DONT_EXIST));
        }

        assert posts != null;
        posts.forEach(post -> post.getUser().setUserPw(null));


        return posts;
    }

    public Map<String, Object> findByUser(User user) {
        Map<String, Object> res = new HashMap<>();
        List<Post> posts = findByUserOrderByCreatedAt(user, "desc");
        int count = countPostsByUser(user);
        posts.forEach(post -> post.setUser(null));
        res.put("userNickName", user.getUserNickName());
        res.put("userIntroduction", user.getUserIntroduction());
        res.put("userIcon", user.getUserIcon());
        res.put("postList", posts);
        res.put("postCount", count);
        return res;
    }

    public int countPostsByUser(User user) {
        return postRepository.countByUser(user).orElseThrow(
                () -> new AppException(ErrorCode.POSTS_DONT_EXIST));
    }

    public Map<String, Post> findRecentPostByUser(User user, Long postId) {
        List<Post> posts = postRepository.findByUserOrderByCreatedAtAsc(user).orElseThrow(() -> new AppException(ErrorCode.POSTS_DONT_EXIST));

        int postIdx = -1;

        for (int i = 0; i < posts.size(); i++) {
            if (posts.get(i).getPostId().equals(postId)) {
                postIdx = i;
                break;
            }
        }

        Post prev = (postIdx > 0) ? posts.get(postIdx - 1) : null;
        Post next = (postIdx < posts.size() - 1) ? posts.get(postIdx + 1) : null;

        Map<String, Post> recentPosts = new HashMap<>();

        recentPosts.put("prev", prev);
        recentPosts.put("next", next);

        return recentPosts;
    }

    public Post findByUserAndPostId(User user, Long postId) {
        Post post = postRepository.findByUserAndPostId(user, postId).orElseThrow(() -> new AppException(ErrorCode.POSTS_DONT_EXIST));

        post.getUser().setUserPw(null);

        return post;
    }

    // U

    // D
    public boolean deletePost(DeletePostRequestDto requestDto, User user) {
        if (!JwtUtil.isTokenOwner(requestDto.getToken(), secretKey, requestDto.getUserEmail())) {
            throw new AppException(ErrorCode.WRONG_ACCEPT);
        }
        Post post = postRepository.findByUserAndPostId(user, requestDto.getPostId()).orElseThrow(
                () -> new AppException(ErrorCode.POSTS_DONT_EXIST)
        );

        postRepository.delete(post);
        return true;
    }

}
