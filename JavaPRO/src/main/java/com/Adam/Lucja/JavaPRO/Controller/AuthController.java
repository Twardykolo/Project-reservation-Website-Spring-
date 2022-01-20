package com.Adam.Lucja.JavaPRO.Controller;

import com.Adam.Lucja.JavaPRO.DTO.Request.AuthRequest;
import com.Adam.Lucja.JavaPRO.DTO.Response.AuthResponse;
import com.Adam.Lucja.JavaPRO.Service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/auth")
    public ResponseEntity<AuthResponse> getToken(@RequestBody AuthRequest authRequest, HttpServletRequest httpServletRequest){
        ResponseEntity<AuthResponse> response;
        try {
            response = ResponseEntity.ok(authService.getSigninCredential(authRequest));
        }catch (AuthenticationException e){
            response = ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return response;
    }
}
