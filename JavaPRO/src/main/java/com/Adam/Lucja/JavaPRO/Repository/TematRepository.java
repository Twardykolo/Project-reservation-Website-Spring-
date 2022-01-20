package com.Adam.Lucja.JavaPRO.Repository;

import com.Adam.Lucja.JavaPRO.Entity.Temat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TematRepository extends JpaRepository<Temat,String>{
    Optional<Temat> findById(Long id);
}
