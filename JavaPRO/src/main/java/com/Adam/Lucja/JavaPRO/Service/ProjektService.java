package com.Adam.Lucja.JavaPRO.Service;

import com.Adam.Lucja.JavaPRO.DTO.Request.ProjektRequest;
import com.Adam.Lucja.JavaPRO.DTO.Response.ProjektResponse;
import com.Adam.Lucja.JavaPRO.Entity.Projekt;
import com.Adam.Lucja.JavaPRO.Entity.Projekt2Student;
import com.Adam.Lucja.JavaPRO.Entity.Student;
import com.Adam.Lucja.JavaPRO.Repository.Projekt2StudentRepository;
import com.Adam.Lucja.JavaPRO.Repository.ProjektRepository;
import com.Adam.Lucja.JavaPRO.Repository.StudentRepository;
import com.Adam.Lucja.JavaPRO.Repository.TematRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjektService {
    @Autowired
    private ProjektRepository projektRepository;

    @Autowired
    private TematRepository tematRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private Projekt2StudentRepository projekt2StudentRepository;

    //Łatwiejszy zapis w pliku TematService.java
    public List<ProjektResponse> getAllProjekty(){
        return projektRepository.findAll().stream() //wyciągnięcie z bazy danych za pomocą studentRepository
                .map(ProjektResponse::new) //przerobienie wyciągniętych danych na obiekty klasy StudentResponse
                .collect(Collectors.toList()); //złapanie wszystkich przerobionych już obiektów do listy
    }
    public ProjektResponse getProjekt(Long id){
        Projekt projekt = projektRepository.getById(id).get();
        return new ProjektResponse(projekt);
    }
    public ProjektResponse createProjekt(ProjektRequest projektRequest){
        Projekt projekt = Projekt.builder()
                .submissionDate(projektRequest.getSubmissionDate())
                .deadline(projektRequest.getDeadline())
                .mark(projektRequest.getMark())
                .build();
        Projekt savedProjekt;
        if(projektRequest.getStudentId()!=null){
            projekt.setTemat(tematRepository.findById(projektRequest.getTematId()).get());
            savedProjekt = projektRepository.save(projekt);
            Student student = studentRepository.findById(projektRequest.getStudentId()).get();
            Projekt2Student projekt2Student = Projekt2Student.builder()
                    .student(student)
                    .projekt(savedProjekt).build();
            projekt.setProjekt2student(new ArrayList<>());
            projekt.getProjekt2student().add(projekt2Student);
            student.setProjekt2student(new ArrayList<>());
            student.getProjekt2student().add(projekt2Student);
            Projekt2Student savedProjekt2Student = projekt2StudentRepository.save(projekt2Student);
            savedProjekt = projektRepository.save(projekt);
        }else
            savedProjekt = projektRepository.save(projekt);

        ProjektResponse zwrotka = new ProjektResponse(savedProjekt);
        return zwrotka;
    }
    public ProjektResponse updateProjekt (ProjektRequest projektRequest, Long id){
        Projekt projekt = projektRepository.getById(id).get();
        projekt.setSubmissionDate(projektRequest.getSubmissionDate());
        projekt.setDeadline(projektRequest.getDeadline());
        projekt.setMark(projektRequest.getMark());

        Projekt savedProjekt = projektRepository.save(projekt);
        return new ProjektResponse(savedProjekt);
    }
    public void deleteProjekt(Long id){
        Projekt projekt = projektRepository.getById(id).get();
        projektRepository.delete(projekt);
    }

}
