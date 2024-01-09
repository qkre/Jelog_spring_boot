package com.example.jelog.web.controller;

import com.example.jelog.service.post.PostService;
import com.example.jelog.web.dto.AddPostRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
