package com.Adam.Lucja.JavaPRO.Service;

import com.Adam.Lucja.JavaPRO.DTO.Request.StudentRequest;
import com.Adam.Lucja.JavaPRO.Entity.Student;
import com.Adam.Lucja.JavaPRO.Repository.StudentRepository;
import com.Adam.Lucja.JavaPRO.DTO.Response.StudentResponse;
import static com.Adam.Lucja.JavaPRO.Util.MD5Generator.getMD5;
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
    public StudentResponse createStudent(StudentRequest studentRequest){
        Student student = Student.builder()
                .name(studentRequest.getName())
                .surname(studentRequest.getSurname())
                .password(getMD5(studentRequest.getPassword()))
                .email(studentRequest.getEmail())
                .nrAlbum(studentRequest.getNrAlbum())
                .build();
        Student savedStudent = studentRepository.save(student);
        StudentResponse zwrotka = new StudentResponse(savedStudent);
        return zwrotka;
    }
}
