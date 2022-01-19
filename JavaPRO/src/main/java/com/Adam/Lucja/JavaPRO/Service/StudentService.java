package com.Adam.Lucja.JavaPRO.Service;

import com.Adam.Lucja.JavaPRO.Repository.StudentRepository;
import com.Adam.Lucja.JavaPRO.Repository.StudentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentService {
    @Autowired
    private StudentRepository studentRepository;
    //Łatwiejszy zapis w pliku TematService.java
    public List<StudentResponse> getAllStudenty(){
        return studentRepository.findAll().stream()
                .map(StudentResponse::new)
                .collect(Collectors.toList());
    }
}
