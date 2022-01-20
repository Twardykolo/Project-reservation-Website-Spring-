package com.Adam.Lucja.JavaPRO.Repository;

import com.Adam.Lucja.JavaPRO.Entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

//Pośrednik między bazą danych, a serwisem
@Repository
public interface StudentRepository extends JpaRepository<Student,String> {
    Student getById(Long id);

    Optional<Student> findByNrAlbum(String nrAlbum);

    Optional<Student> findByNrAlbumOrEmail(String userLoginOrEmail, String userLoginOrEmail1);

    Optional<Student> findById(Long id);
}
