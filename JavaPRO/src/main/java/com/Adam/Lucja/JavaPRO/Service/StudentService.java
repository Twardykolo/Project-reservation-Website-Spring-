package com.Adam.Lucja.JavaPRO.Service;

import com.Adam.Lucja.JavaPRO.DTO.Request.LoginRequest;
import com.Adam.Lucja.JavaPRO.DTO.Request.StudentRequest;
import com.Adam.Lucja.JavaPRO.Entity.Student;
import com.Adam.Lucja.JavaPRO.Repository.StudentRepository;
import com.Adam.Lucja.JavaPRO.DTO.Response.StudentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@Transactional
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private LoginService loginService;

    //Łatwiejszy zapis w pliku TematService.java
    public List<StudentResponse> getAllStudenty(){
        return studentRepository.findAll().stream() //wyciągnięcie z bazy danych za pomocą studentRepository
                .map(StudentResponse::new) //przerobienie wyciągniętych danych na obiekty klasy StudentResponse
                .collect(Collectors.toList()); //złapanie wszystkich przerobionych już obiektów do listy
    }
    public StudentResponse getStudent(Long id){
        Student student = studentRepository.getById(id);
        return new StudentResponse(student);
    }
    public Student getStudentByNrAlbumu(String nrAlbumu){
        Student student = studentRepository.findByNrAlbum(nrAlbumu).get();
        return student;
    }
    public StudentResponse createStudent(StudentRequest studentRequest){
        Student student = Student.builder()
                .name(studentRequest.getName())
                .surname(studentRequest.getSurname())
                .login(
                        loginService.createLogin(new LoginRequest(studentRequest))
                )
                .email(studentRequest.getEmail().toLowerCase(Locale.ROOT))
                .nrAlbum(studentRequest.getNrAlbum())
                .build();
        Student savedStudent = studentRepository.save(student);
        StudentResponse zwrotka = new StudentResponse(savedStudent);
        return zwrotka;
    }
    public StudentResponse updateStudent (StudentRequest studentRequest, Long id){
        Student student = studentRepository.getById(id);
        student.setName(studentRequest.getName());
        student.setSurname(studentRequest.getSurname());
        student.setEmail(studentRequest.getEmail());
        student.setNrAlbum(studentRequest.getNrAlbum());

        Student savedStudent = studentRepository.save(student);
        return new StudentResponse(savedStudent);
    }
    public void deleteStudent(Long id){
        Student student = studentRepository.getById(id);
        studentRepository.delete(student);
    }
}
