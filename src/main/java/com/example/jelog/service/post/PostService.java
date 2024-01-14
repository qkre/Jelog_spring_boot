package com.example.jelog.service.post;

import com.example.jelog.domain.post.Post;
import com.example.jelog.domain.post.PostLike;
import com.example.jelog.domain.user.User;
import com.example.jelog.exception.AppException;
import com.example.jelog.exception.ErrorCode;
import com.example.jelog.repository.PostLikeRepository;
import com.example.jelog.repository.PostRepository;
import com.example.jelog.repository.UserRepository;
import com.example.jelog.web.dto.AddPostRequestDto;
import com.example.jelog.web.dto.LikePostRequestDto;
import com.example.jelog.web.dto.UnlikePostRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostLikeRepository postLikeRepository;

    // C
    public Long write(AddPostRequestDto requestDto) {
        User user = userRepository.findByUserEmail(requestDto.getUserEmail()).orElseThrow(
                () -> new AppException(ErrorCode.USEREMAIL_NOTEXIST, "존재하지 않는 계정입니다."));

        return postRepository.save(
                Post.builder()
                        .user(user)
                        .title(requestDto.getTitle())
                        .content(requestDto.getContent())
                        .tags(requestDto.getTags())
                        .build()
        ).getPostId();
    }

    public boolean likePost(LikePostRequestDto requestDto) {
        Post post = postRepository.findById(requestDto.getPostId()).orElseThrow(() -> new AppException(ErrorCode.POSTS_NOTEXIST, "존재하지 않는 포스터"));

        Set<PostLike> postLikes = post.getPostLike();

        boolean alreadyLikedUser = postLikes.stream().anyMatch(postLike -> postLike.getUserId().equals(requestDto.getUserId()));

        if (!alreadyLikedUser) {
            postLikes.add(PostLike.builder().userId(requestDto.getUserId()).build());
            postRepository.save(post);

            return true;
        }

        return false;
    }

    public boolean unLikePost(UnlikePostRequestDto requestDto) {
        Post post = postRepository.findById(requestDto.getPostId()).orElseThrow(() -> new AppException(ErrorCode.POSTS_NOTEXIST, "존재하지 않는 포스트"));

        Set<PostLike> postLikes = post.getPostLike();

        boolean alreadyLikedUser = postLikes.stream().anyMatch(postLike -> postLike.getUserId().equals(requestDto.getUserId()));

        if(alreadyLikedUser) {
            PostLike target = postLikeRepository.findByPostIdAndUserId(requestDto.getPostId(), requestDto.getUserId()).orElseThrow(() -> new AppException(ErrorCode.WRONG_ACCEPT, "잘못된 접근"));

            postLikes.remove(target);

            postRepository.save(post);
           return true;
        }

        return false;
    }

    // R
    public List<Post> getPostsOrderByCreatedAtDesc() {
        List<Post> posts = postRepository.findAllByOrderByCreatedAtDesc().orElseThrow(() -> new AppException(ErrorCode.POSTS_NOTEXIST, "포스트가 존재하지 않습니다."));
        if(!posts.isEmpty()) {
            posts.forEach(post -> post.getUser().setUserPw(null));
        }

        return posts;

    }

//    public List<Post> getPostsOrderByPostLikes(){
//
//    }

    public Post getPost(String userNickName, Long postId) {
        User user = userRepository.findByUserNickName(userNickName).orElseThrow(
                () -> new AppException(ErrorCode.USEREMAIL_NOTEXIST, userNickName + "는 존재하지 않는 계정입니다.")
        );

        Post post = postRepository.findByUserAndPostId(user, postId).orElseThrow(() -> new AppException(ErrorCode.POSTS_NOTEXIST, "포스트가 존재하지 않습니다."));

        post.getUser().setUserPw(null);

        return post;
    }

    // U

    // D
}
