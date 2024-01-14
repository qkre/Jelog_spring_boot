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

import java.util.*;

@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostLikeRepository postLikeRepository;

    // C
    public Long write(AddPostRequestDto requestDto) {
        User user = userRepository.findByUserEmail(requestDto.getUserEmail()).orElseThrow(
                () -> new AppException(ErrorCode.USER_DONT_EXIST, "존재하지 않는 계정입니다."));

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
        Post post = postRepository.findById(requestDto.getPostId()).orElseThrow(() -> new AppException(ErrorCode.POSTS_DONT_EXIST, "존재하지 않는 포스터"));

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
        Post post = postRepository.findById(requestDto.getPostId()).orElseThrow(() -> new AppException(ErrorCode.POSTS_DONT_EXIST, "존재하지 않는 포스트"));

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
        List<Post> posts = postRepository.findAllByOrderByCreatedAtDesc().orElseThrow(() -> new AppException(ErrorCode.POSTS_DONT_EXIST, "포스트가 존재하지 않습니다."));
        if(!posts.isEmpty()) {
            posts.forEach(post -> post.getUser().setUserPw(null));
        }

        return posts;

    }

    public List<Post> getPostsByUserId(Long userId){
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_DONT_EXIST, "존재하지 않는 계정입니다."));
        List<Post> posts = postRepository.findByUser(user).orElseThrow(() -> new AppException(ErrorCode.POSTS_DONT_EXIST, "포스트가 존재하지 않습니다."));

        posts.forEach(post -> post.getUser().setUserPw(null));

        return posts;
    }

    public Map<String, Post> getRecentPostsByUserId(Long userId, Long postId){
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_DONT_EXIST, "존재하지 않는 계정입니다."));
        List<Post> posts = postRepository.findByUserOrderByCreatedAtAsc(user).orElseThrow(() -> new AppException(ErrorCode.POSTS_DONT_EXIST, "포스트가 존재하지 않습니다."));



        int postIdx = -1;

        for(int i = 0; i < posts.size(); i++){
            if(posts.get(i).getPostId().equals(postId)){
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

    public Post getPost(String userNickName, Long postId) {
        User user = userRepository.findByUserNickName(userNickName).orElseThrow(
                () -> new AppException(ErrorCode.USER_DONT_EXIST, userNickName + "는 존재하지 않는 계정입니다.")
        );

        Post post = postRepository.findByUserAndPostId(user, postId).orElseThrow(() -> new AppException(ErrorCode.POSTS_DONT_EXIST, "포스트가 존재하지 않습니다."));

        post.getUser().setUserPw(null);

        return post;
    }

    // U

    // D
}
