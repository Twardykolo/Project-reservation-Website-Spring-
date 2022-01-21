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

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.Adam.Lucja.JavaPRO.Util.ERole.ROLE_ADMIN;

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

    public Object registerStudent(StudentRequest studentRequest) {
        if(studentRepository.existsByNrAlbum(studentRequest.getNrAlbum()))
            return new MessageResponse("Taki użytkownik już istnieje");
        if(studentRepository.existsByEmail(studentRequest.getEmail()))
            return new MessageResponse("Ten email już został wykorzystany");
        StudentResponse studentResponse = studentService.createStudent(studentRequest);
        Set<String> strRoles = new HashSet<>();
        Role role = roleRepository.findByName(ERole.ROLE_USER).get();
        strRoles.add(role.toString());
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        Login login = loginRepository.findByUsername(studentResponse.getNrAlbum()).get();
        login.setRoles(roles);
        loginRepository.save(login);
        return studentResponse;
    }

    public AuthResponse getAuthToken(AuthRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtTokenProvider.generateToken(authentication);
        UserDetailsImpl userDetailsImpl = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetailsImpl.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());
        return new AuthResponse(userDetailsImpl.getId(), jwt, userDetailsImpl.getUsername(), userDetailsImpl.getEmail(), roles);
    }

    public Object getAdminToken(AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        UserDetailsImpl userDetailsImpl = (UserDetailsImpl) authentication.getPrincipal();
        if(!userDetailsImpl.getAuthorities().stream().anyMatch(
                a -> a.getAuthority().equals("ROLE_ADMIN")
        ))
            return new MessageResponse("Brak uprawnień administratora");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtTokenProvider.generateToken(authentication);
        List<String> roles = userDetailsImpl.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());
        return new AuthResponse(userDetailsImpl.getId(), jwt, userDetailsImpl.getUsername(), userDetailsImpl.getEmail(), roles);
    }
}
