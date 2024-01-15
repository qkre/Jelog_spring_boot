package com.example.jelog.jwt;

import com.example.jelog.service.user.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final String secretKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);


        if(authorization == null || !authorization.startsWith("Bearer ")){
            logger.error("Authorization 이 없습니다.");
            filterChain.doFilter(request, response);
            return;
        }

        // Token 꺼내기
        String token = authorization.split(" ")[1];

        // Token 유효 검사
        if(JwtUtil.isExpired(token, secretKey)){
            logger.error("만료된 Token 입니다.");
            filterChain.doFilter(request, response);
        }

        // UserEmail 꺼내기;
        String userEmail = JwtUtil.getClaimUserEmail(token, secretKey);

        logger.info(userEmail);

        logger.info("유효 토큰 입니다.");




        // 권한 부여
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userEmail, null, List.of(new SimpleGrantedAuthority("USER")));

        // Detail 삽입
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);

    }
}
