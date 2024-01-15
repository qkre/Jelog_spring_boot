package com.example.jelog.web.controller;

import com.example.jelog.domain.user.User;
import com.example.jelog.service.user.UserService;
import com.example.jelog.web.dto.user.AddUserRequestDto;
import com.example.jelog.web.dto.user.DeleteUserRequestDto;
import com.example.jelog.web.dto.user.GetUserDetailRequestDto;
import com.example.jelog.web.dto.user.LoginRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class UserApiController {

    private final UserService userService;

    // 회원 가입
    @PostMapping("/public/user")
    public ResponseEntity<String> signUp(@RequestBody AddUserRequestDto requestDto){
        Long userId = userService.register(requestDto);

        return ResponseEntity.ok(String.format("회원 번호 %d 번으로 가입 하셨습니다..", userId));
    }

    // 회원 탈퇴
    @DeleteMapping("/private/user")
    public ResponseEntity<String> delete(@RequestBody DeleteUserRequestDto requestDto){
        if(userService.delete(requestDto)){
            return ResponseEntity.ok("회원 탈퇴 완료");
        }
        return ResponseEntity.badRequest().body("올바른 요청이 아닙니다.");
    }

    // 로그인
    @PostMapping("/public/user/login")
    public ResponseEntity<HashMap<String, String>> login(@RequestBody LoginRequestDto requestDto){
        String token = userService.login(requestDto);
        HashMap<String, String> response = new HashMap<>();
        response.put("message", String.format("%s 계정으로 로그인 되었습니다.", requestDto.getUserEmail()));
        response.put("token", token);
        return ResponseEntity.ok(response);
    }

    // 계정 정보
    @GetMapping("/private/user")
    public ResponseEntity<User> getUserDetail(@RequestParam String userEmail, @RequestParam String token){
        User user = userService.findByEmail(userEmail, token);
        user.setUserPw(null);
        return ResponseEntity.ok(user);
    }

    // ID 조회
    @GetMapping("/public/user/valid")
    public ResponseEntity<Boolean> userEmailValidCheck(@RequestParam String userEmail){
        boolean result = userService.userEmailValidCheck(userEmail);
        return ResponseEntity.ok(result);
    }

}
