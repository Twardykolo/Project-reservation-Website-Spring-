package com.Adam.Lucja.JavaPRO.Repository;

import com.Adam.Lucja.JavaPRO.Entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//Pośrednik między bazą danych, a serwisem
@Repository
public interface StudentRepository extends JpaRepository<Student,String> {
}
