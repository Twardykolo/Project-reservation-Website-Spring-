package com.Adam.Lucja.JavaPRO.Repository;

import com.Adam.Lucja.JavaPRO.Entity.Login;
import com.Adam.Lucja.JavaPRO.Entity.Login2Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Login2RoleRepository extends JpaRepository<Login2Role,String> {
}
