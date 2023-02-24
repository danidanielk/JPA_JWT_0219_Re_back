package com.kim.dani.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.util.Base64;


@Component
public class JwtTokenValidator {

//    @Value("${jwt.secret}")
//    private static String jwtSecret;

    private static String jwtSecret="daniel";
    private static byte[] secret= Base64.getEncoder().encode(jwtSecret.getBytes());



    public String jwtGetUserEmail(HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("ddoken")) {

                String token = cookie.getValue();
                System.out.println(token+"%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
                Claims claims = Jwts.parser()
                        .setSigningKey(secret)
                        .parseClaimsJws(token).getBody();
                if (claims != null){
                    String email = (String) claims.get("email");
                    return email;
                }else{
                    return null;
                }
            }
        }
        return null;
    }



}