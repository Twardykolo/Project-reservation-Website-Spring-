package com.Adam.Lucja.JavaPRO.Service;

import com.Adam.Lucja.JavaPRO.DTO.Request.ProjektRequest;
import com.Adam.Lucja.JavaPRO.DTO.Response.ProjektResponse;
import com.Adam.Lucja.JavaPRO.Entity.Projekt;
import com.Adam.Lucja.JavaPRO.Entity.Student;
import com.Adam.Lucja.JavaPRO.Repository.ProjektRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjektService {
    @Autowired
    private ProjektRepository projektRepository;
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
        Projekt savedProjekt = projektRepository.save(projekt);
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
