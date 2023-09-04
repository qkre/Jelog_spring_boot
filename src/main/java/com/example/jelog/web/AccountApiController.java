package com.example.jelog.web;

import com.example.jelog.domain.account.Account;
import com.example.jelog.service.AccountService;
import com.example.jelog.web.dto.AddAccountRequestDto;
import com.example.jelog.web.dto.LoginRequestDto;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Controller
@RequestMapping("/api")
public class AccountApiController {

    private final AccountService accountService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> signup(@RequestBody AddAccountRequestDto requestDto){
        accountService.save(requestDto);

        Map<String, Object> response = new HashMap<>();
        response.put("Message", "Account created Successfully");

        return  ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDto requestDto){
        System.out.println("requestDto = " + requestDto.getEmail());
        System.out.println("requestDto = " + requestDto.getPassword());
        Boolean result = accountService.login(requestDto);
        System.out.println("result = " + result);

        if(result) return ResponseEntity.ok("Login Success");
        else return ResponseEntity.badRequest().build();
    }

    @GetMapping("/login/{email}")
    public ResponseEntity<String> getAccount(@PathVariable String email){
        return ResponseEntity.ok(accountService.getAccount(email).toJson());
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response){
        return "redirect:/login";
    }

}
