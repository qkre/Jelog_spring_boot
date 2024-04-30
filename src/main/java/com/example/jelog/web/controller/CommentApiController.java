package com.example.jelog.web.controller;

import com.example.jelog.service.comment.CommentService;
import com.example.jelog.web.dto.comment.AddCommentRequestDto;
import com.example.jelog.web.dto.comment.ModifyCommentRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/private/comment")
public class CommentApiController {

    private final CommentService commentService;

    @PostMapping()
    public ResponseEntity<String> writeComment(@RequestBody AddCommentRequestDto requestDto){
        boolean result = commentService.write(requestDto);

        return ResponseEntity.ok("등록 결과 : " + result);
    }

    @PutMapping()
    public ResponseEntity<String> modifyComment(@RequestBody ModifyCommentRequestDto requestDto){
        boolean result = commentService.modify(requestDto);

        return ResponseEntity.ok("수정 결과 : " + result);
    }
}
