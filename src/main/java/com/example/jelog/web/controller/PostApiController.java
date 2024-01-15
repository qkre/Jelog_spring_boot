package com.example.jelog.web.controller;

import com.example.jelog.domain.post.Post;
import com.example.jelog.service.post.PostService;
import com.example.jelog.web.dto.post.AddPostRequestDto;
import com.example.jelog.web.dto.post.DeletePostRequestDto;
import com.example.jelog.web.dto.post.LikePostRequestDto;
import com.example.jelog.web.dto.post.UnlikePostRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class PostApiController {
    private final PostService postService;

    // 글 작성
    @PostMapping("/private/post")
    public ResponseEntity<String> write(@RequestBody AddPostRequestDto requestDto){
        boolean result = postService.write(requestDto);

        if(result) return ResponseEntity.ok("게시글 등록 완료");
        else return ResponseEntity.badRequest().body("게시글 등록 실패");
    }

    // 글 불러오기
    @GetMapping("/public/post/all")
    public ResponseEntity<List<Post>> getPosts(@RequestParam String orderBy){
        List<Post> posts = postService.getPostsOrderByCreatedAtDesc();


        return ResponseEntity.ok(posts);
    }

    @GetMapping("/public/post/all/by")
    public ResponseEntity<List<Post>> getPostsByUserId(@RequestParam Long userId) {
        List<Post> posts = postService.getPostsByUserId(userId);

        return ResponseEntity.ok(posts);
    }

    @GetMapping("/public/post/recent")
    public ResponseEntity<Map<String, Post>> getRecentPosts(@RequestParam Long userId, @RequestParam Long postId){
        Map<String, Post> recentPosts = postService.getRecentPostsByUserId(userId, postId);

        return ResponseEntity.ok(recentPosts);
    }
    @GetMapping("/public/post/{userNickName}/{postId}")
    public ResponseEntity<Post> getPost(@PathVariable String userNickName, @PathVariable Long postId){
        Post post = postService.getPost(userNickName, postId);
        post.getUser().setUserPw(null);
        return ResponseEntity.ok(post);
    }


    // 게시글 추천하기
    @PostMapping("/private/post/like")
    public ResponseEntity<String> likePost(@RequestBody LikePostRequestDto requestDto){
        boolean result = postService.likePost(requestDto);

        return ResponseEntity.ok(String.format("추천이 반영 되었는가 ? %b", result));
    }

    @DeleteMapping("/private/post/like")
    public ResponseEntity<String> UnlikePost(@RequestBody UnlikePostRequestDto requestDto) {
        boolean result = postService.unLikePost(requestDto);

        return ResponseEntity.ok(String.format("추천이 반영 되었는가 ? %b", result));
    }

    @DeleteMapping("/private/post")
    public ResponseEntity<String> deletePost(@RequestBody DeletePostRequestDto requestDto){
        boolean result = postService.deletePost(requestDto);

        return ResponseEntity.ok("게시글이 삭제되었습니다.");
    }
}
