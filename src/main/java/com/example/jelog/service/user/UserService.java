package com.example.jelog.service.user;

import com.example.jelog.domain.post.Post;
import com.example.jelog.domain.user.User;
import com.example.jelog.exception.AppException;
import com.example.jelog.exception.ErrorCode;
import com.example.jelog.jwt.JwtUtil;
import com.example.jelog.repository.UserRepository;
import com.example.jelog.service.post.PostService;
import com.example.jelog.web.dto.user.AddUserRequestDto;
import com.example.jelog.web.dto.user.DeleteUserRequestDto;
import com.example.jelog.web.dto.user.LoginRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class UserService {

    @Value("${jwt.secret}")
    private String secretKey;
    private Long expiredMs = 1000 * 30 * 60 * 60L;

    private final PostService postService;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    // C : 회원 등록
    public Long register(AddUserRequestDto requestDto) {
        userRepository.findByUserEmail(requestDto.getUserEmail()).ifPresent(user -> {
            throw new AppException(ErrorCode.USER_EMAIL_DUPLICATED);
        });

        return userRepository.save(
                User.builder()
                        .userEmail(requestDto.getUserEmail())
                        .userNickName(requestDto.getUserEmail().split("@")[0])
                        .userPw(encoder.encode(requestDto.getUserPw()))
                        .userIcon(requestDto.getUserIcon())
                        .build()
        ).getUserId();
    }

    // R : 로그인
    public String login(LoginRequestDto requestDto) {

        User user = userRepository.findByUserEmail(requestDto.getUserEmail())
                .orElseThrow(() -> new AppException(ErrorCode.USER_DONT_EXIST));

        if (!encoder.matches(requestDto.getUserPw(), user.getUserPw())) {
            throw new AppException(ErrorCode.INVALID_PASSWORD);
        }

        return JwtUtil.createJwt(requestDto.getUserEmail(), secretKey, expiredMs);
    }

    public User findByEmail(String userEmail, String token) {

        if (!JwtUtil.isTokenOwner(token, secretKey, userEmail)) {
            throw new AppException(ErrorCode.WRONG_ACCEPT);
        }

        return userRepository.findByUserEmail(userEmail).orElseThrow(
                () -> new AppException(ErrorCode.USER_DONT_EXIST)
        );
    }

    public User findByUserNickName(String userNickName){
        return userRepository.findByUserNickName(userNickName).orElseThrow(() -> new AppException(ErrorCode.USER_DONT_EXIST));
    }

    public boolean userEmailValidCheck(String userEmail) {
        boolean isValid = userRepository.findByUserEmail(userEmail).isPresent();

        return isValid;
    }

    public User findByUserEmail(String userEmail) {
        return userRepository.findByUserEmail(userEmail).orElseThrow(() ->
                new AppException(ErrorCode.USER_DONT_EXIST)
        );
    }

    public User findById(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () ->
                        new AppException(ErrorCode.USER_DONT_EXIST)
        );
    }
    // U : 회원 정보 변경

    // D : 회원 삭제
    public boolean delete(DeleteUserRequestDto requestDto) {
        if (!JwtUtil.isTokenOwner(requestDto.getToken(), secretKey, requestDto.getUserEmail())) {
            throw new AppException(ErrorCode.WRONG_ACCEPT);
        }
        try {
            User user = userRepository.findByUserEmail(requestDto.getUserEmail()).orElseThrow(
                    () -> new AppException(ErrorCode.USER_DONT_EXIST)
            );
            userRepository.delete(user);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
