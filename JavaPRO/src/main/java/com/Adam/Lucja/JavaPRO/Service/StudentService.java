package com.Adam.Lucja.JavaPRO.Service;

import com.Adam.Lucja.JavaPRO.DTO.Request.LoginRequest;
import com.Adam.Lucja.JavaPRO.DTO.Request.StudentRequest;
import com.Adam.Lucja.JavaPRO.Entity.Student;
import com.Adam.Lucja.JavaPRO.Repository.StudentRepository;
import com.Adam.Lucja.JavaPRO.DTO.Response.StudentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.Adam.Lucja.JavaPRO.Entity.Login;
import java.util.ArrayList;
import java.util.stream.Stream;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Klasa serwisowa do obsługi Studentów ({@link Student})
 */
@Service
@Transactional //adnotacja zmieniająca tryb komunikacji z bazą danych, dzięki czemu można wybierać duże dane (np. pliki z bazy)
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private LoginService loginService;

    //Bardziej przejrzysty zapis w pliku TematService.java (funkcja getAllTematy)

    /**
     * Metoda odszukująca wszystkich {@link Student}ów w bazie danych za pomocą {@link StudentRepository}.
     * Lista odnalezionych studentów zostaje potraktowana jako strumień ({@link java.util.stream.Stream}).
     * Elementy znajdujące się w tym strumieniu zostają zmapowane do obiektów zwrotnych typu {@link StudentResponse},
     * a następnie ponownie zebrane do postaci listy ({@link java.util.ArrayList}).
     * W efekcie otrzymujemy {@link List<StudentResponse>}ę wypełnioną obiektami zwrotnymi.
     * @return {@link List<StudentResponse>}
     */
    public List<StudentResponse> getAllStudenty(){
        return studentRepository.findAll().stream() //wyciągnięcie z bazy danych za pomocą studentRepository
                .map(StudentResponse::new) //przerobienie wyciągniętych danych na obiekty klasy StudentResponse
                .collect(Collectors.toList()); //złapanie wszystkich przerobionych już obiektów do listy
    }

    /**
     * Metoda odszukująca {@link Student}a o podanym id ({@link Long}) w bazie za pomocą {@link StudentRepository}.
     * Na podstawie odszukanego studenta zostaje utworzony obiekt zwrotny klasy {@link StudentResponse}.
     * @param id Id szukanego studenta ({@link Long})
     * @return {@link StudentResponse}
     */
    public StudentResponse getStudent(Long id){
        Student student = studentRepository.getById(id);
        return new StudentResponse(student);
    }
    /**
     * Metoda odszukująca {@link Student}a o podanym nrAlbumu ({@link String}) w bazie za pomocą {@link StudentRepository}.
     * Zwraca obiekt znalezionego studenta.
     * @param nrAlbumu nrAlbumu szukanego studenta ({@link String})
     * @return {@link Student}
     */
    public Student getStudentByNrAlbumu(String nrAlbumu){
        Student student = studentRepository.findByNrAlbum(nrAlbumu).get();
        return student;
    }

    /**
     * Metoda tworzy nowego {@link Student}a na podstawie danych przekazanych przy pomocy obiektu pomocniczego
     * {@link StudentRequest}. W trakcie twrzoenia studenta za pomocą Buildera (Lombok), utworzony zostaje
     * także odpowiadający mu obiekt typu {@link Login} służący później do logowania.
     * Tworzeniem loginu zajmuje się {@link LoginService}. Utworzony student zostaje zapisany w bazie danych
     * za pomocą {@link StudentRepository}, a reprezentacja jego zapisu zwrócona w postaci obiektu
     * typu {@link StudentResponse}.
     * @param studentRequest Obiekt pomocniczy przechowujący dane potrzebne do utworzenia {@link Student}a i {@link Login}u
     * @return {@link StudentResponse}
     */
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

    /**
     * Metoda aktualizująca dane {@link Student}a o podanym Id ({@link Long}) na podstawie danych
     * przekazanych przez obiekt pomocniczy {@link StudentRequest}. Student zostaje odszukany w bazie danych
     * na podstawie Id przez {@link StudentRepository}. Dane otrzymanego studenta zostają zastąpione danymi przekazanymi
     * w obiekcie pomocniczym, a następnie zmodyfikowany student zostaje zapisany przez {@link StudentRepository}.
     * Reprezentacja zapisanego obiektu zostaje przekazana w postaci {@link StudentResponse} jako wynik funkcji.
     * @param studentRequest Obiekt pomocniczy z danymi potrzebnymi do utworzenia {@link Student}a
     * @param id Id edytowanego studenta({@link Long})
     * @return {@link StudentResponse}
     */
    public StudentResponse updateStudent (StudentRequest studentRequest, Long id){
        Student student = studentRepository.getById(id);
        student.setName(studentRequest.getName());
        student.setSurname(studentRequest.getSurname());
        student.setEmail(studentRequest.getEmail());
        student.setNrAlbum(studentRequest.getNrAlbum());

        Student savedStudent = studentRepository.save(student);
        return new StudentResponse(savedStudent);
    }

    /**
     * Metoda usuwająca {@link Student}a o podanym Id ({@link Long}) z bazy danych.
     * Student zostaje odszukany przez {@link StudentRepository} na podstawie przekazanego Id, a następnie
     * usunięty za pomocą metody delete.
     * @param id Id usuwanego {@link Student}a, typ {@link Long}
     */
    public void deleteStudent(Long id){
        Student student = studentRepository.getById(id);
        studentRepository.delete(student);
    }
}
