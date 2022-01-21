package com.Adam.Lucja.JavaPRO.Repository;

import com.Adam.Lucja.JavaPRO.Entity.Role;
import com.Adam.Lucja.JavaPRO.Util.ERole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
  Optional<Role> findByName(ERole name);
}
