package com.example.jelog.web.controller;

import com.example.jelog.domain.post.Post;
import com.example.jelog.domain.user.User;
import com.example.jelog.service.post.PostService;
import com.example.jelog.service.user.UserService;
import com.example.jelog.web.dto.post.AddPostRequestDto;
import com.example.jelog.web.dto.post.DeletePostRequestDto;
import com.example.jelog.web.dto.post.LikePostRequestDto;
import com.example.jelog.web.dto.post.UnlikePostRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class PostApiController {
    private final PostService postService;
    private final UserService userService;
    // 글 작성
    @PostMapping("/private/post")
    public ResponseEntity<String> write(@RequestBody AddPostRequestDto requestDto){
        User user = userService.findByUserEmail(requestDto.getUserEmail());
        boolean result = postService.write(requestDto, user);

        if(result) return ResponseEntity.ok("게시글 등록 완료");
        else return ResponseEntity.badRequest().body("게시글 등록 실패");
    }

    // 글 불러오기
    @GetMapping("/public/post/all")
    public ResponseEntity<Page<Post>> getPosts(@RequestParam String orderBy, @PageableDefault(size = 10, sort = "postId") Pageable pageable){
        Page<Post> posts;
        if (orderBy.equals("createdAt")) {
            posts = postService.findAllOrderByCreatedAtDesc(pageable);
        } else {
            posts = postService.findAll(pageable);
        }

        return ResponseEntity.ok(posts);
    }

    @GetMapping("/public/post/all/by")
    public ResponseEntity<List<Post>> getPostsByUserId(@RequestParam Long userId, @PageableDefault(size = 10, sort = "postId") Pageable pageable) {
        User user = userService.findById(userId);
        List<Post> posts = postService.findByUserOrderByCreatedAt(user, "desc");

        return ResponseEntity.ok(posts);
    }

    @GetMapping("/public/post/recent")
    public ResponseEntity<Map<String, Post>> getRecentPosts(@RequestParam Long userId, @RequestParam Long postId, @PageableDefault Pageable pageable){
        User user = userService.findById(userId);

        Map<String, Post> recentPosts = postService.findRecentPostByUser(user, postId);

        return ResponseEntity.ok(recentPosts);
    }
    @GetMapping("/public/post/{userNickName}/{postId}")
    public ResponseEntity<Post> getPostByPostIdAndUser(@PathVariable String userNickName, @PathVariable Long postId){
        User user = userService.findByUserNickName(userNickName);
        Post post = postService.findByUserAndPostId(user, postId);
        post.getUser().setUserPw(null);
        return ResponseEntity.ok(post);
    }


    @GetMapping("/public/post")
    public ResponseEntity<Map<String, Object>> getUserDetail(@RequestParam String userNickName){
        User user = userService.findByUserNickName(userNickName);
        Map<String, Object> res = postService.findByUser(user);
        return ResponseEntity.ok(res);
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
        User user = userService.findByUserEmail(requestDto.getUserEmail());
        boolean result = postService.deletePost(requestDto, user);

        return ResponseEntity.ok("게시글 삭제 결과 : " + result);
    }

}
