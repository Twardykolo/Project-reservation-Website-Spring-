package com.Adam.Lucja.JavaPRO.Controller;

import com.Adam.Lucja.JavaPRO.DTO.Request.AuthRequest;
import com.Adam.Lucja.JavaPRO.Service.AuthService;
import com.Adam.Lucja.JavaPRO.Service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class LoginController {
    @Autowired
    LoginService loginService;

    @Autowired
    AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> adminLogin(@RequestBody AuthRequest authRequest){
        return ResponseEntity.ok(authService.getAdminToken(authRequest));
    }
}
