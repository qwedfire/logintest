package org.example.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.Key;
import java.security.KeyStore;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class JWTokenCreator {
    public static void main(String[] args)  {
        getJwtToken();
    }

    public static String getJwtToken() {
        Instant now = Instant.now();
        byte[] secret = Keys.secretKeyFor(SignatureAlgorithm.HS256).getEncoded();
        String secretValue = Base64.getEncoder().encodeToString(secret);
        System.out.println(secretValue);
        String jwt = Jwts.builder()
//                .setSubject("user123")
//                .setAudience("user1234")
                .claim("name", "user1234")
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(1, ChronoUnit.MINUTES)))
                .signWith(Keys.hmacShaKeyFor(secret))
                .compact();

        System.out.println(jwt);

        Jws<Claims> result=Jwts.parser()
                .setSigningKey(Keys.hmacShaKeyFor(secret))
                .parseClaimsJws(jwt);
        System.out.println(result);
//        System.out.println("1d20: "+result.getBody().get("1d20",Integer.class));
        return jwt;
    }
}
