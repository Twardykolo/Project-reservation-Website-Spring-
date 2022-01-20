package com.Adam.Lucja.JavaPRO.Service;

import com.Adam.Lucja.JavaPRO.Config.LoginPrincipal;
import com.Adam.Lucja.JavaPRO.Entity.Student;
import com.Adam.Lucja.JavaPRO.Repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ExpressionException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

@Service
public class LoginDetailsService implements UserDetailsService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private HttpServletRequest request;

    @Transactional
    public UserDetails loadUserByUsername(String userLoginOrEmail)  {

        Student student = studentRepository.findByNrAlbumOrEmail(userLoginOrEmail, userLoginOrEmail)
                .orElseThrow(() -> new ExpressionException("User Not Found"));

        return LoginPrincipal.create(student);
    }

    @Transactional
    public UserDetails loadUserById(String id)  {
        Student login = studentRepository.findByNrAlbum(id)
                .orElseThrow(() -> new ExpressionException("User Not Found"));
        LoginPrincipal loginPrincipal = LoginPrincipal.create(login);

        return loginPrincipal;
    }

}
