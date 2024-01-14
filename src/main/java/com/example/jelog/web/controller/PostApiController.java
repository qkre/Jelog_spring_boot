package com.example.jelog.web.controller;

import com.example.jelog.domain.post.Post;
import com.example.jelog.service.post.PostService;
import com.example.jelog.web.dto.AddPostRequestDto;
import com.example.jelog.web.dto.LikePostRequestDto;
import com.example.jelog.web.dto.UnlikePostRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/post")
public class PostApiController {
    private final PostService postService;

    // 글 작성
    @PostMapping("/write")
    public ResponseEntity<String> write(@RequestBody AddPostRequestDto requestDto){
        Long postId = postService.write(requestDto);

        return ResponseEntity.ok(String.format("게시글 번호 %d 번으로 글을 등록하였습니다.", postId));
    }

    // 글 불러오기
    @GetMapping("/all")
    public ResponseEntity<List<Post>> getPosts(@RequestParam String orderBy){
        List<Post> posts = postService.getPostsOrderByCreatedAtDesc();
//        posts.forEach(post -> {
//            post.getUser().setUserPw(null);
//        });

        return ResponseEntity.ok(posts);
    }

    @GetMapping("/read/{userNickName}/{postId}")
    public ResponseEntity<Post> getPost(@PathVariable String userNickName, @PathVariable Long postId){
        Post post = postService.getPost(userNickName, postId);
//        post.getUser().setUserPw(null);
        return ResponseEntity.ok(post);
    }

    // 게시글 추천하기
    @PostMapping("/like/add")
    public ResponseEntity<String> likePost(@RequestBody LikePostRequestDto requestDto){
        boolean result = postService.likePost(requestDto);

        return ResponseEntity.ok(String.format("추천이 반영 되었는가 ? %b", result));
    }

    @DeleteMapping("/like/delete")
    public ResponseEntity<String> UnlikePost(@RequestBody UnlikePostRequestDto requestDto) {
        boolean result = postService.unLikePost(requestDto);

        return ResponseEntity.ok(String.format("추천이 반영 되었는가 ? %b", result));
    }
}
