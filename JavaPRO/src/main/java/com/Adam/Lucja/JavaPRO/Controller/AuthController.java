package com.Adam.Lucja.JavaPRO.Controller;

import com.Adam.Lucja.JavaPRO.DTO.Request.AuthRequest;
import com.Adam.Lucja.JavaPRO.DTO.Request.StudentRequest;
import com.Adam.Lucja.JavaPRO.DTO.Response.MessageResponse;
import com.Adam.Lucja.JavaPRO.DTO.Response.StudentResponse;
import com.Adam.Lucja.JavaPRO.Entity.Login;
import com.Adam.Lucja.JavaPRO.Entity.Role;
import com.Adam.Lucja.JavaPRO.Repository.LoginRepository;
import com.Adam.Lucja.JavaPRO.Repository.RoleRepository;
import com.Adam.Lucja.JavaPRO.Security.JWTTokenProvider;
import com.Adam.Lucja.JavaPRO.Security.LoginPrincipal;
import com.Adam.Lucja.JavaPRO.DTO.Response.AuthResponse;
import com.Adam.Lucja.JavaPRO.Repository.StudentRepository;
import com.Adam.Lucja.JavaPRO.Service.StudentService;
import com.Adam.Lucja.JavaPRO.Util.ERole;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    LoginRepository loginRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    StudentService studentService;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JWTTokenProvider jwtTokenProvider;

    @PostMapping("/signin")
    public ResponseEntity<?> getToken(@Valid @RequestBody AuthRequest loginRequest){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtTokenProvider.generateToken(authentication);
        LoginPrincipal loginPrincipal = (LoginPrincipal) authentication.getPrincipal();
        List<String> roles = loginPrincipal.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());
        return ResponseEntity.ok(new AuthResponse(loginPrincipal.getLogin().getId(),jwt, loginPrincipal.getUsername(), loginPrincipal.getPassword(),roles));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerStudent(@Valid @RequestBody StudentRequest studentRequest){
        if(studentRepository.existsByNrAlbum(studentRequest.getNrAlbum()))
            return ResponseEntity.badRequest().body(new MessageResponse("Taki użytkownik już istnieje"));
        if(studentRepository.existsByEmail(studentRequest.getEmail()))
            return ResponseEntity.badRequest().body(new MessageResponse("Ten email już został wykorzystany"));
        StudentResponse studentResponse = studentService.createStudent(studentRequest);
        Set<String> strRoles = new HashSet<>();
        Role role = roleRepository.findByName(ERole.ROLE_USER).get();
        strRoles.add(role.toString());
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        Login login = loginRepository.findByUsername(studentResponse.getNrAlbum()).get();
        login.setRoles(roles);
        loginRepository.save(login);
        return ResponseEntity.ok(studentResponse);
    }
}
