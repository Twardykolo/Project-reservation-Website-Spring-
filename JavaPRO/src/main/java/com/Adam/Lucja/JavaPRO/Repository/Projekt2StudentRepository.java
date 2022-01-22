package com.Adam.Lucja.JavaPRO.Repository;

import com.Adam.Lucja.JavaPRO.Entity.Projekt2Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Projekt2StudentRepository extends JpaRepository<Projekt2Student,String> {
    List<Projekt2Student> findAllByStudentId(Long id);
    List<Projekt2Student> findAllByProjektId(Long id);
}
