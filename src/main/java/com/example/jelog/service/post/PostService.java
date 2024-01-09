package com.example.jelog.service.post;

import com.example.jelog.domain.post.Post;
import com.example.jelog.domain.user.User;
import com.example.jelog.repository.PostRepository;
import com.example.jelog.service.user.UserService;
import com.example.jelog.web.dto.AddPostRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;
    private final UserService userService;

    // C
    public Long write(AddPostRequestDto requestDto){
        User user = userService.findByEmail(requestDto.getUserEmail());

        return postRepository.save(
                Post.builder()
                        .user(user)
                        .title(requestDto.getTitle())
                        .content(requestDto.getContent())
                        .build()
        ).getPostId();
    }

    // R

    // U

    // D
}
