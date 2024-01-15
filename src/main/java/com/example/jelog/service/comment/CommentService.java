package com.example.jelog.service.comment;

import com.example.jelog.domain.comment.Comment;
import com.example.jelog.domain.post.Post;
import com.example.jelog.domain.user.User;
import com.example.jelog.exception.AppException;
import com.example.jelog.exception.ErrorCode;
import com.example.jelog.jwt.JwtUtil;
import com.example.jelog.repository.PostRepository;
import com.example.jelog.repository.UserRepository;
import com.example.jelog.web.dto.comment.AddCommentRequestDto;
import com.example.jelog.web.dto.comment.ModifyCommentRequestDto;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CommentService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Value("${jwt.secret}")
    private String secretKey;

    // C
    public boolean write(AddCommentRequestDto requestDto){
        if(!JwtUtil.isTokenOwner(requestDto.getToken(), secretKey, requestDto.getUserEmail())){
            throw new AppException(ErrorCode.WRONG_ACCEPT, "잘못된 접근입니다.");
        }

        User user = userRepository.findByUserEmail(requestDto.getUserEmail()).orElseThrow(
                () -> new AppException(ErrorCode.USER_DONT_EXIST, "존재하지 않는 유저입니다.")
        );
        Post post = postRepository.findById(requestDto.getPostId()).orElseThrow(
                () -> new AppException(ErrorCode.POSTS_DONT_EXIST, "존재하지 않는 포스터입니다."));

        List<Comment> comments = post.getComments();

        Comment newComment = Comment.builder()
                .user(user)
                .content(requestDto.getContent())
                .build();
        comments.add(newComment);

        postRepository.save(post);

        return true;
    }

    // R

    // U
    public boolean modify(ModifyCommentRequestDto requestDto){
        if(!JwtUtil.isTokenOwner(requestDto.getToken(), secretKey, requestDto.getUserEmail())){
            throw new AppException(ErrorCode.WRONG_ACCEPT, "잘못된 접근입니다.");
        }

        User user = userRepository.findByUserEmail(requestDto.getUserEmail()).orElseThrow(
                () -> new AppException(ErrorCode.USER_DONT_EXIST, "존재하지 않는 유저입니다.")
        );
        Post post = postRepository.findById(requestDto.getPostId()).orElseThrow(
                () -> new AppException(ErrorCode.POSTS_DONT_EXIST, "존재하지 않는 포스터입니다."));

        List<Comment> comments = post.getComments();

        for (Comment comment : comments) {
            if (comment.getCommentId().equals(requestDto.getCommentId())) {
                comment.update(requestDto.getContent());
                break;
            }
        }

        postRepository.save(post);

        return true;

    }

    // D
}
