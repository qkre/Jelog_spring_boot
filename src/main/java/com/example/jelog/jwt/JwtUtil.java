package com.example.jelog.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JwtUtil {

    public static final String CLAIM_USER_EMAIL = "userEmail";
    public static String createJwt(String userEmail, String secretKey, Long expiredMs){

        return Jwts.builder()
                .claim(CLAIM_USER_EMAIL, userEmail)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public static boolean isExpired(String token, String secretKey){
        return Jwts.parser()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration()
                .before(new Date());
    }

    public static boolean isTokenOwner(String token, String secretKey, String userEmail) {
        String tokenOwner = getClaimUserEmail(token, secretKey);
        return userEmail.equals(tokenOwner);
    }

    public static String getClaimUserEmail(String token, String secretKey) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("userEmail", String.class);
    }

}
