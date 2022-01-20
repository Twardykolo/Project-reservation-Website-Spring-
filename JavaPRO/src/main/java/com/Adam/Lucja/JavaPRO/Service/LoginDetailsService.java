package com.Adam.Lucja.JavaPRO.Service;

import com.Adam.Lucja.JavaPRO.Entity.Login;
import com.Adam.Lucja.JavaPRO.Repository.LoginRepository;
import com.Adam.Lucja.JavaPRO.Security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ExpressionException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class LoginDetailsService implements UserDetailsService {

    @Autowired
    private LoginRepository studentRepository;

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Login login = studentRepository.findByUsername(username)
                .orElseThrow(() -> new ExpressionException("User Not Found"));

        return UserDetailsImpl.build(login);
    }
}
