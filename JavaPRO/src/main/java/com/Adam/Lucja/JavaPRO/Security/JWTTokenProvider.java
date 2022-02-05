package com.Adam.Lucja.JavaPRO.Security;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Locale;

@Component
public class JWTTokenProvider {

    private static final Logger log = LoggerFactory.getLogger(JWTTokenProvider.class);
    /**
     * Wartości pól zostają pobrane z pliku application.properties za pomocą adnotacji {@link Value}
     */
    @Value("${security.authentication.jwt.secret}")
    private String secretKey;

    @Value("${security.authentication.jwt.token-validity}")
    private Long tokenValidity;

    /**
     * Funkcja generująca token JWT na podstawie loginu użytkownika uzyskanego z przekazanego obiektu {@link Authentication},
     * aktualnej daty {@link Date}, daty wygaśnięcia tworzonej z daty aktualnej powiększonej o wartość zmiennej globalnej
     * tokenValidity będącej ilością milisekund i sekretnego klucza szyfrowania secretKey (także zmienna globalna).
     * Token zostaje wygenerowany za pomocą klasy {@link Jwts} i zwrócony jako {@link String}
     * @param authentication Obiekt reprezentujący aktualną autoryzację
     * @return {@link String}
     */
    public String generateToken(Authentication authentication) {
        UserDetailsImpl userDetailsImpl = (UserDetailsImpl) authentication.getPrincipal();
        return Jwts.builder()
                .setSubject(userDetailsImpl.getUsername().toLowerCase(Locale.ROOT))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime()+tokenValidity))
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    /**
     * Funkcja odszyfrowująca ukryty w przekazanym tokenie JWT login użytkownika.
     * Token zostaje odszyfrowany przy pomocy klasy {@link Jwts} i sekretnego klucza szyfrowania secretKey.
     * Jako wynik funkcja zwraca login użytkownika w postaci {@link String}
     * @param token Token przeznaczony do odszyfrowania
     * @return {@link String}
     */
    public String getUsernameFromJWT(String token) {
        return Jwts.parser().setSigningKey(secretKey)
                .parseClaimsJws(token).getBody()
                .getSubject();
    }

    /**
     * Funkcja zajmująca się walidacją  przekazanego tokenu autoryzacyjnego.
     * Token zostaje sprawdzony przy użyciu sekretnego klucza szyfrowania secretKey oraz przekazanego tokenu
     * przy pomocy klasy {@link Jwts}. Jeśli walidacja sie powiedzie, zwrócona zostaje wartość true,
     * w przeciwnym wypadku przechwycony zostanie odpowiedni wyjątek i zwrócona wartość false.
     * @param authToken Token do walidacji
     * @return {@link Boolean}
     */
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
