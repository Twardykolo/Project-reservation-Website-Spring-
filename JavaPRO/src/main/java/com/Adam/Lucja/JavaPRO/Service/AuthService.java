package com.Adam.Lucja.JavaPRO.Service;

import com.Adam.Lucja.JavaPRO.DTO.Request.AuthRequest;
import com.Adam.Lucja.JavaPRO.DTO.Request.StudentRequest;
import com.Adam.Lucja.JavaPRO.DTO.Response.AuthResponse;
import com.Adam.Lucja.JavaPRO.DTO.Response.MessageResponse;
import com.Adam.Lucja.JavaPRO.DTO.Response.StudentResponse;
import com.Adam.Lucja.JavaPRO.Entity.Login;
import com.Adam.Lucja.JavaPRO.Entity.Role;
import com.Adam.Lucja.JavaPRO.Repository.LoginRepository;
import com.Adam.Lucja.JavaPRO.Repository.RoleRepository;
import com.Adam.Lucja.JavaPRO.Repository.StudentRepository;
import com.Adam.Lucja.JavaPRO.Security.JWTTokenProvider;
import com.Adam.Lucja.JavaPRO.Security.UserDetailsImpl;
import com.Adam.Lucja.JavaPRO.Util.ERole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContext;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.Adam.Lucja.JavaPRO.Util.ERole.ROLE_ADMIN;

/**
 * Klasa serwisowa związana z autoryzacją
 */
@Service
public class AuthService {

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    StudentService studentService;

    @Autowired
    LoginRepository loginRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JWTTokenProvider jwtTokenProvider;

    /**
     * Metoda generująca token autoryzacji JWT na podstawie danych logowania przekazanych poprzez obiekt {@link AuthRequest}.
     * Login i hasło użyte do logowania zostają wprowadzone do funkcji authenticate {@link AuthenticationManager}a (Spring Security),
     * a w odpowiedzi otrzymany zostaje obiekt klasy {@link Authentication} będący reprezentacją autoryzacji.
     * Następnie autoryzacja ta zostaje przekazana do zabezpieczeń (Spring Security) poprzez
     * wyciągnięcie aktualnego {@link SecurityContext} za pomocą {@link SecurityContextHolder}a i wywołaniu settera
     * dla pola autenthication. Następnie wygenerowany zostaje token JWT przy użyciu klasy {@link JWTTokenProvider}.
     * Na podstawie wcześniej otrzymanej reprezentacji autoryzacji, uzyskane zostają także dane logowanego użytkownika
     * w postaci obiektu {@link UserDetailsImpl}. Kolejnie uzyskana zostaje lista ról przypisanych użytkownikowi i zebrana do
     * {@link List}y składającej się z obiektów typu {@link String}. Finalnie zwrócony zostaje obiekt zwrotny typu {@link AuthResponse}
     * składający się z danych użytkownika, tokenu JWT, ról użytkownika.
     * @param authRequest Obiekt przekazujący dane logowania
     * @return {@link AuthResponse}
     */
    public AuthResponse getAuthToken(AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtTokenProvider.generateToken(authentication);
        UserDetailsImpl userDetailsImpl = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetailsImpl.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());
        return new AuthResponse(userDetailsImpl.getId(), jwt, userDetailsImpl.getUsername(), userDetailsImpl.getEmail(), roles);
    }
}
