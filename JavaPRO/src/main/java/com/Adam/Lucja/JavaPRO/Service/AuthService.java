package com.Adam.Lucja.JavaPRO.Service;

import com.Adam.Lucja.JavaPRO.Security.JWTTokenProvider;
import com.Adam.Lucja.JavaPRO.Security.LoginPrincipal;
import com.Adam.Lucja.JavaPRO.DTO.Request.LoginRequest;
import com.Adam.Lucja.JavaPRO.DTO.Response.AuthResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthService {

    private final Logger log = LoggerFactory.getLogger(AuthService.class);
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JWTTokenProvider jwtTokenProvider;

    public AuthResponse getSigninCredential(LoginRequest loginRequest) {

        log.debug("Try login by user {}", loginRequest.getUsername());

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtTokenProvider.generateToken(authentication);

        LoginPrincipal loginPrincipal = (LoginPrincipal) authentication.getPrincipal();
        List<String> roles = loginPrincipal.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return new AuthResponse(
                ((LoginPrincipal) authentication.getPrincipal()).getLogin().getId(),
                jwt,
                ((LoginPrincipal) authentication.getPrincipal()).getLogin().getUsername(),
                ((LoginPrincipal) authentication.getPrincipal()).getLogin().getEmail(),
                roles
        );
    }
}
