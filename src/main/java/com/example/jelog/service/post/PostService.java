package com.example.jelog.service.post;

import com.example.jelog.domain.post.Post;
import com.example.jelog.domain.post.PostLike;
import com.example.jelog.domain.user.User;
import com.example.jelog.exception.AppException;
import com.example.jelog.exception.ErrorCode;
import com.example.jelog.jwt.JwtUtil;
import com.example.jelog.repository.PostLikeRepository;
import com.example.jelog.repository.PostRepository;
import com.example.jelog.repository.UserRepository;
import com.example.jelog.web.dto.post.AddPostRequestDto;
import com.example.jelog.web.dto.post.DeletePostRequestDto;
import com.example.jelog.web.dto.post.LikePostRequestDto;
import com.example.jelog.web.dto.post.UnlikePostRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import java.util.*;

@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostLikeRepository postLikeRepository;

    @Value("${jwt.secret}")
    private String secretKey;

    // C
    public boolean write(AddPostRequestDto requestDto) {
        if(!JwtUtil.isTokenOwner(requestDto.getToken(), secretKey, requestDto.getUserEmail())){
            throw new AppException(ErrorCode.WRONG_ACCEPT, "잘못된 접근입니다.");
        }

        User user = userRepository.findByUserEmail(requestDto.getUserEmail()).orElseThrow(
                () -> new AppException(ErrorCode.USER_DONT_EXIST, "존재하지 않는 계정입니다."));

        postRepository.save(
                Post.builder()
                        .user(user)
                        .title(requestDto.getTitle())
                        .content(requestDto.getContent())
                        .tags(requestDto.getTags())
                        .build()
        );

        return true;
    }

    public boolean likePost(LikePostRequestDto requestDto) {
        if(!JwtUtil.isTokenOwner(requestDto.getToken(), secretKey, requestDto.getUserEmail())){
            throw new AppException(ErrorCode.WRONG_ACCEPT, "잘못된 접근입니다.");
        }

        Post post = postRepository.findById(requestDto.getPostId()).orElseThrow(() -> new AppException(ErrorCode.POSTS_DONT_EXIST, "존재하지 않는 포스터"));

        Set<PostLike> postLikes = post.getPostLikes();

        boolean alreadyLikedUser = postLikes.stream().anyMatch(postLike -> postLike.getUserEmail().equals(requestDto.getUserEmail()));

        if (!alreadyLikedUser) {
            postLikes.add(PostLike.builder().userEmail(requestDto.getUserEmail()).build());
            postRepository.save(post);

            return true;
        }

        return false;
    }

    public boolean unLikePost(UnlikePostRequestDto requestDto) {
        if(!JwtUtil.isTokenOwner(requestDto.getToken(), secretKey, requestDto.getUserEmail())){
            throw new AppException(ErrorCode.WRONG_ACCEPT, "잘못된 접근입니다.");
        }

        Post post = postRepository.findById(requestDto.getPostId()).orElseThrow(() -> new AppException(ErrorCode.POSTS_DONT_EXIST, "존재하지 않는 포스트"));

        Set<PostLike> postLikes = post.getPostLikes();

        boolean alreadyLikedUser = postLikes.stream().anyMatch(postLike -> postLike.getUserEmail().equals(requestDto.getUserEmail()));

        if(alreadyLikedUser) {
            PostLike target = postLikeRepository.findByPostIdAndUserEmail(requestDto.getPostId(), requestDto.getUserEmail()).orElseThrow(() -> new AppException(ErrorCode.WRONG_ACCEPT, "잘못된 접근"));

            postLikes.remove(target);

            postRepository.save(post);
           return true;
        }

        return false;
    }

    // R
    public List<Post> getPostsOrderByCreatedAtDesc(Pageable pageable) {
        List<Post> posts = postRepository.findAllByOrderByCreatedAtDesc(pageable).orElseThrow(
                () -> new AppException(ErrorCode.POSTS_DONT_EXIST, "포스트가 존재하지 않습니다."));
        posts.forEach(post -> post.getUser().setUserPw(null));
        return posts;
    }

    public List<Post> getPostsOrderByPostLikesAtDesc(){
        List<Post> posts = postRepository.findAll();
        posts.sort(
                Collections.reverseOrder(
                        Comparator.comparingInt(
                                post -> post.getPostLikes().size())
                )
        );
        return posts;
    }

    public List<Post> getPostsByUserId(Long userId){
        User user = userRepository.findById(userId).orElseThrow(
                () -> new AppException(ErrorCode.USER_DONT_EXIST, "존재하지 않는 계정입니다.")
        );
        List<Post> posts = postRepository.findByUser(user).orElseThrow(
                () -> new AppException(ErrorCode.POSTS_DONT_EXIST, "포스트가 존재하지 않습니다.")
        );

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
    public boolean deletePost(DeletePostRequestDto requestDto){
        if(!JwtUtil.isTokenOwner(requestDto.getToken(), secretKey, requestDto.getUserEmail())){
            throw new AppException(ErrorCode.WRONG_ACCEPT, "잘못된 접근입니다.");
        }
        User user = userRepository.findByUserEmail(requestDto.getUserEmail()).orElseThrow(
                () -> new AppException(ErrorCode.USER_DONT_EXIST, "존재하지 않는 계정입니다.")
        );
        Post post = postRepository.findByUserAndPostId(user, requestDto.getPostId()).orElseThrow(
                () -> new AppException(ErrorCode.POSTS_DONT_EXIST,"존재하지 않는 포스트입니다.")
        );

        postRepository.delete(post);
        return true;
    }




    public void makeDummyData(){
        for (int i = 0; i < 300; i++){
            User user = User.builder().userNickName(i+"번째 유저").build();

            userRepository.save(user);

            Post post = Post.builder()
                    .user(user)
                    .title(i + "번째 게시글")
                    .content(i + "번째 내용")
                    .build();

            postRepository.save(post);
        }
    }
}
