package com.example.jelog.service.post;

import com.example.jelog.domain.post.Post;
import com.example.jelog.domain.user.User;
import com.example.jelog.exception.AppException;
import com.example.jelog.exception.ErrorCode;
import com.example.jelog.repository.PostRepository;
import com.example.jelog.repository.UserRepository;
import com.example.jelog.web.dto.AddPostRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    // C
    public Long write(AddPostRequestDto requestDto) {
        User user = userRepository.findByUserEmail(requestDto.getUserEmail()).orElseThrow(
                () -> new AppException(ErrorCode.USEREMAIL_NOTEXIST, requestDto.getUserEmail() + "는 존재하지 않는 계정입니다."));

        return postRepository.save(
                Post.builder()
                        .user(user)
                        .title(requestDto.getTitle())
                        .content(requestDto.getContent())
                        .tags(requestDto.getTags())
                        .build()
        ).getPostId();
    }

    // R
    public List<Post> getPostsOrderByCreatedAtDesc(){
        return postRepository.findAllByOrderByCreatedAtDesc().orElseThrow(() -> new AppException(ErrorCode.POSTS_NOTEXIST, "포스트가 존재하지 않습니다."));
    }


    // U

    // D
}
