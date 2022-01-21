package com.Adam.Lucja.JavaPRO.Security;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JWTTokenProvider {

    private static final Logger log = LoggerFactory.getLogger(JWTTokenProvider.class);

    @Value("${security.authentication.jwt.secret}")
    private String secretKey;

    @Value("${security.authentication.jwt.token-validity}")
    private Long tokenValidity;

    public String generateToken(Authentication authentication) {

        UserDetailsImpl userDetailsImpl = (UserDetailsImpl) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject(userDetailsImpl.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime()+tokenValidity))
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    public String getNrAlbumFromJWT(String token) {
        return Jwts.parser().setSigningKey(secretKey)
                .parseClaimsJws(token).getBody()
                .getSubject();
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(authToken);
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
