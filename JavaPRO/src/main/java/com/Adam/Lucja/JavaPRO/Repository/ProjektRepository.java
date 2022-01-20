package com.Adam.Lucja.JavaPRO.Repository;

import com.Adam.Lucja.JavaPRO.Entity.Projekt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjektRepository extends JpaRepository<Projekt,String> {
    Optional<Projekt> findById(Long id);
}
