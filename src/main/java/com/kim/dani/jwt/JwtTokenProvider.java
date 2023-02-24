package com.kim.dani.jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtTokenProvider {

//    @Value("${jwt.secret}")
//    private static String jwtSecret;

    private static String jwtSecret="daniel";
    private static byte[] secret= Base64.getEncoder().encode(jwtSecret.getBytes());


    public String generateToken(String email) {
        Date now = new Date(System.currentTimeMillis());
        Date expiryDate = new Date(System.currentTimeMillis()+60*30000);

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .claim("email",email)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public String setTokenCookie(HttpServletResponse res,String email){
//        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider();
        String token = generateToken(email);
        System.out.println(token+"===========================");
        Cookie cookie = new Cookie("ddoken", token);
        cookie.setMaxAge(60*10000);
        cookie.setHttpOnly(true); // Xss 방어
        cookie.setPath("/");
        res.addCookie(cookie);
        return token;

    }
}