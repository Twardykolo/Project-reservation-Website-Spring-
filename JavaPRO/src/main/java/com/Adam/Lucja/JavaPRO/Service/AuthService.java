package com.Adam.Lucja.JavaPRO.Service;

import com.Adam.Lucja.JavaPRO.Config.JWTTokenProvider;
import com.Adam.Lucja.JavaPRO.Config.LoginPrincipal;
import com.Adam.Lucja.JavaPRO.DTO.Request.AuthRequest;
import com.Adam.Lucja.JavaPRO.DTO.Response.AuthResponse;
import com.Adam.Lucja.JavaPRO.Entity.Student;
import com.Adam.Lucja.JavaPRO.Repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class AuthService {

    private final Logger log = LoggerFactory.getLogger(AuthService.class);
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JWTTokenProvider jwtTokenProvider;

    public AuthResponse getSigninCredential(AuthRequest authRequest) {

        log.debug("Try login by user {}", authRequest.getLoginOrEmail());

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(authRequest.getLoginOrEmail(), authRequest.getPassword());
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtTokenProvider.generateToken(authentication);


        return new AuthResponse(
                ((LoginPrincipal) authentication.getPrincipal()).getLogin().getId(),
                jwt,
                ((LoginPrincipal) authentication.getPrincipal()).getLogin().getNrAlbum(),
                ((LoginPrincipal) authentication.getPrincipal()).getLogin().getEmail()
        );
    }
}
