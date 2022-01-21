package com.Adam.Lucja.JavaPRO.Repository;

import com.Adam.Lucja.JavaPRO.Entity.Login;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoginRepository  extends JpaRepository<Login,String> {
    Optional<Login> findByUsername(String username);
    Optional<Login> findByEmail(String email);
}
