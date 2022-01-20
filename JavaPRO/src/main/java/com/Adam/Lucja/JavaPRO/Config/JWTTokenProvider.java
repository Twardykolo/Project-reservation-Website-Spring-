package com.Adam.Lucja.JavaPRO.Config;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;

@Component
public class JWTTokenProvider {

    private static final Logger log = LoggerFactory.getLogger(JWTTokenProvider.class);

    private final Base64.Encoder encoder = Base64.getEncoder();

    @Value("${security.authentication.jwt.secret}")
    private String secretKey;

    @Value("${security.authentication.jwt.token-validity}")
    private Long tokenValidity;

    public String generateToken(Authentication authentication) {

        LoginPrincipal loginPrincipal = (LoginPrincipal) authentication.getPrincipal();

        long now = (new Date()).getTime();
        Date expiryDate = new Date(now + this.tokenValidity);

        return Jwts.builder()
                .setSubject(loginPrincipal.getLogin().getNrAlbum())
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, this.secretKey)
                .compact();
    }

    public String getLoginIdFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(this.secretKey)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public boolean validateToken(String authToken) {

        try {
            Jwts.parser().setSigningKey(this.secretKey).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            log.info("SignatureException in validateToken()");
            log.trace("SignatureException in validateToken(): {}", e);
        } catch (MalformedJwtException e) {
            log.info("MalformedJwtException in validateToken()");
            log.trace("MalformedJwtException in validateToken(): {}", e);

        } catch (UnsupportedJwtException e) {
            log.info("UnsupportedJwtException in validateToken()");
            log.trace("UnsupportedJwtException in validateToken(): {}", e);
        } catch (IllegalArgumentException e) {
            log.info("IllegalArgumentException in validateToken()");
            log.trace("IllegalArgumentException in validateToken(): {}", e);
        }
        return false;
    }

}
