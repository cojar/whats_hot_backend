package com.cojar.whats_hot_backend.global.jwt;

import com.cojar.whats_hot_backend.global.util.AppConfig;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

@Component
public class JwtProvider {

    @Value("${custom.jwt.secretKey}")
    private String originalKey;

    private SecretKey secretKey;

    private SecretKey genSecretKey() {

        String keyBase64Encoded = Base64.getEncoder().encodeToString(this.originalKey.getBytes());

        return Keys.hmacShaKeyFor(keyBase64Encoded.getBytes());
    }

    private SecretKey getSecretKey() {

        if (this.secretKey == null) this.secretKey = this.genSecretKey();

        return this.secretKey;
    }

    public String genToken(Map<String, Object> claims, int seconds) {

        long now = new Date().getTime(); // milliseconds
        Date tokenExpirationDate = new Date(now + 1000L * seconds); // seconds to milliseconds

        return Jwts.builder()
                .claim("body", AppConfig.toJson(claims))
                .expiration(tokenExpirationDate)
                .signWith(this.getSecretKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public boolean verify(String accessToken) {

        try {
            // secretKey를 통해 accessToken을 꺼내는 과정
            // 만료 시 에러 발생
            Jwts.parser()
                    .verifyWith(this.getSecretKey())
                    .build()
                    .parseSignedClaims(accessToken);
        } catch (Exception e) {
            // 만료되었으므로 false 반환
            return false;
        }

        // 에러 미발생 시 유효하므로 true 반환
        return true;
    }

    public Map<String, Object> getClaims(String accessToken) {

        String body = Jwts.parser()
                .verifyWith(this.getSecretKey())
                .build()
                .parseSignedClaims(accessToken)
                .getPayload()
                .get("body", String.class);

        return AppConfig.toMap(body);
    }
}
