package com.example.jelog.web.controller;

import com.example.jelog.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/jwt")
public class JwtApiController {

    @Value("${jwt.secret}")
    private String secretKey;
    @GetMapping("/valid")
    public ResponseEntity<Boolean> isValidToken(@RequestParam String token){
        return ResponseEntity.ok(JwtUtil.isExpired(token, secretKey));
    }
}
