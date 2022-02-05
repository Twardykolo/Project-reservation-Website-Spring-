package com.Adam.Lucja.JavaPRO.Controller;

import com.Adam.Lucja.JavaPRO.DTO.Request.AuthRequest;
import com.Adam.Lucja.JavaPRO.DTO.Request.StudentRequest;
import com.Adam.Lucja.JavaPRO.Repository.LoginRepository;
import com.Adam.Lucja.JavaPRO.Repository.RoleRepository;
import com.Adam.Lucja.JavaPRO.Security.JWTTokenProvider;
import com.Adam.Lucja.JavaPRO.Security.UserDetailsImpl;
import com.Adam.Lucja.JavaPRO.DTO.Response.AuthResponse;
import com.Adam.Lucja.JavaPRO.Repository.StudentRepository;
import com.Adam.Lucja.JavaPRO.Service.AuthService;
import com.Adam.Lucja.JavaPRO.Service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthService authService;

    @PostMapping("/signin")
    public ResponseEntity<?> getToken(@Valid @RequestBody AuthRequest loginRequest){
        return ResponseEntity.ok(authService.getAuthToken(loginRequest));
    }

}
