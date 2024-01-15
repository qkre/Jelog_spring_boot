package com.example.jelog.service.user;

import com.example.jelog.domain.user.User;
import com.example.jelog.exception.AppException;
import com.example.jelog.exception.ErrorCode;
import com.example.jelog.jwt.JwtUtil;
import com.example.jelog.repository.UserRepository;
import com.example.jelog.web.dto.user.AddUserRequestDto;
import com.example.jelog.web.dto.user.DeleteUserRequestDto;
import com.example.jelog.web.dto.user.GetUserDetailRequestDto;
import com.example.jelog.web.dto.user.LoginRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    @Value("${jwt.secret}")
    private String secretKey;
    private Long expiredMs = 1000 * 30 * 60 * 60L;

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    // C : 회원 등록
    public Long register(AddUserRequestDto requestDto) {
        userRepository.findByUserEmail(requestDto.getUserEmail()).ifPresent(user -> {
            throw new AppException(ErrorCode.USER_EMAIL_DUPLICATED, requestDto.getUserEmail() + "는 이미 존재하는 계정입니다.");
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
                .orElseThrow(() -> new AppException(ErrorCode.USER_DONT_EXIST, requestDto.getUserEmail() + "는 존재하지 않는 계정입니다."));

        if (!encoder.matches(requestDto.getUserPw(), user.getUserPw())) {
            throw new AppException(ErrorCode.INVALID_PASSWORD, "패스워드가 일치하지 않습니다.");
        }

        return JwtUtil.createJwt(requestDto.getUserEmail(), secretKey, expiredMs);
    }

    public User findByEmail(String userEmail, String token) {

        if(!JwtUtil.isTokenOwner(token, secretKey, userEmail)){
            throw new AppException(ErrorCode.WRONG_ACCEPT, "잘못된 요청입니다.");
        }

        return userRepository.findByUserEmail(userEmail).orElseThrow(
                () -> new AppException(ErrorCode.USER_DONT_EXIST, "존재하지 않는 사용자")
        );
    }

    public boolean userEmailValidCheck(String userEmail) {
        boolean isValid = userRepository.findByUserEmail(userEmail).isPresent();

        return isValid;
    }
    // U : 회원 정보 변경

    // D : 회원 삭제
    public boolean delete(DeleteUserRequestDto requestDto) {
        if(!JwtUtil.isTokenOwner(requestDto.getToken(), secretKey, requestDto.getUserEmail())){
            throw new AppException(ErrorCode.WRONG_ACCEPT, "잘못된 접근입니다.");
        }
        try {
            User user = userRepository.findByUserEmail(requestDto.getUserEmail()).orElseThrow(
                    () -> new AppException(ErrorCode.USER_DONT_EXIST, "존재하지 않는 유저입니다.")
            );
            userRepository.delete(user);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
