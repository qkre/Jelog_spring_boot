package com.example.jelog.web.controller;

import com.example.jelog.domain.user.User;
import com.example.jelog.service.user.UserService;
import com.example.jelog.web.dto.AddUserRequestDto;
import com.example.jelog.web.dto.LoginRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserApiController {

    private final UserService userService;

    // 회원 가입
    @PostMapping("/register")
    public ResponseEntity<String> signUp(@RequestBody AddUserRequestDto requestDto){
        Long userId = userService.register(requestDto);

        return ResponseEntity.ok(String.format("회원 번호 %d 번으로 가입 하셨습니다..", userId));
    }

    // 회원 탈퇴
    @DeleteMapping("/{userId}/delete")
    public ResponseEntity<String> delete(@PathVariable Long userId){
        if(userService.delete(userId)){
            return ResponseEntity.ok(String.format("회원 번호 %d 번 님이 탈퇴 하셨습니다.", userId));
        }
        return ResponseEntity.badRequest().body("올바른 요청이 아닙니다.");
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<HashMap<String, String>> login(@RequestBody LoginRequestDto requestDto){
        String token = userService.login(requestDto);
        HashMap<String, String> response = new HashMap<>();
        response.put("message", String.format("%s 계정으로 로그인 되었습니다.", requestDto.getUserEmail()));
        response.put("token", token);
        return ResponseEntity.ok(response);
    }

    // 계정 정보
    @GetMapping("/detail")
    public ResponseEntity<User> getUserData(@RequestParam String userEmail){
        User user = userService.findByEmail(userEmail);
        user.setUserPw(null);
        return ResponseEntity.ok(user);
    }

    // ID 조회
    @GetMapping("/valid")
    public ResponseEntity<Boolean> userEmailValidCheck(@RequestParam String userEmail){
        System.out.println("userEmail = " + userEmail);
        userService.findByEmail(userEmail);
        return ResponseEntity.ok(false);
    }
}
