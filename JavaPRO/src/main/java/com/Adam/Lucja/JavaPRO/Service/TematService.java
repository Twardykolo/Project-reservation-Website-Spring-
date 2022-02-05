package com.Adam.Lucja.JavaPRO.Service;

import com.Adam.Lucja.JavaPRO.DTO.Request.ProjektRequest;
import com.Adam.Lucja.JavaPRO.DTO.Request.TematRequest;
import com.Adam.Lucja.JavaPRO.DTO.Response.TematResponse;
import com.Adam.Lucja.JavaPRO.Entity.Projekt;
import com.Adam.Lucja.JavaPRO.Entity.Temat;
import com.Adam.Lucja.JavaPRO.Repository.FileRepository;
import com.Adam.Lucja.JavaPRO.Repository.TematRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.stream.Stream;
import com.Adam.Lucja.JavaPRO.Entity.Student;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static java.util.function.Predicate.not;

/**
 * Klasa serwisowa do obsługi Tematów ({@link Temat})
 */
@Service
public class TematService {
    @Autowired
    private TematRepository tematRepository;

    @Autowired
    private ProjektService projektService;

    /**
     * Metoda odszukująca wszystkie tematy ({@link Temat}) w bazie danych za pomocą {@link TematRepository}.
     * Odnalezione tematy zostają wykorzystane do utworzenia listy obiektów zwrotnych dla klasy {@link Temat} - {@link TematResponse}.
     * Zwraca listę obiektów zwrotnych typu {@link TematResponse}.
     * @return {@link List<TematResponse>}
     */
    public List<TematResponse> getAllTematy(){
        List<Temat> tematy =tematRepository.findAll();
        List<TematResponse> wynikFunkcji = new ArrayList<>();
        for (Temat temat: tematy) {
            wynikFunkcji.add(new TematResponse(temat));
        }
        return wynikFunkcji;
    }
    /**
     * Metoda odszukująca wszystkie dostępne tematy ({@link Temat}) z bazy danych.
     * Uzyskuje listę tematów za pomocą {@link TematRepository}.
     * Każdy temat zostaje zweryfikowany, czy odpowiadający mu {@link Projekt} nie został już oddany / oceniony.
     * Odnalezione tematy zostają wykorzystane do utworzenia listy obiektów zwrotnych dla klasy {@link Temat} - {@link TematResponse}.
     * Zwraca listę obiektów zwrotnych typu {@link TematResponse}.
     * @return {@link List<TematResponse>}
     */
    public List<TematResponse> getAllAviableTematy(){
        List<Temat> tematy =tematRepository.findAll();
        List<TematResponse> wynikFunkcji = new ArrayList<>();
        for (Temat temat: tematy) {
            try{
                Projekt projekt = projektService.getOneProjektByTematId(temat.getId());
                if(projekt.getFile()!=null || projekt.getMark()!=null)
                    continue;
            }catch (Exception e){
            }
            wynikFunkcji.add(new TematResponse(temat));
        }
        return wynikFunkcji;
    }

    /**
     * Metoda odszukująca {@link Temat} o podanym id w bazie danych za pomocą {@link TematRepository}.
     * Dla odszukanego tematu zostaje utworzony obiekt zwrotny dla klasy {@link Temat} - typu {@link TematResponse}
     * @param id Id szukanego tematu (typu {@link Long})
     * @return {@link TematResponse}
     */
    public TematResponse getTemat(Long id){
        return new TematResponse(tematRepository.findById(id).get());
    }

    /**
     * Metoda tworząca nowy {@link Temat} na podstawie danych przekazanych
     * w obiekcie pomocnicznym typu {@link TematRequest}. Utworzony obiekt
     * zostaje zapisany w bazie danych za pomocą {@link TematRepository}
     * a zapisana postać zwrócona w postaci obiektu zwrotnego typu {@link TematResponse}.
     * @param tematRequest Obiekt typu {@link TematRequest} przechowujący potrzebne dane
     * @return {@link TematResponse}
     */
    public TematResponse createTemat(TematRequest tematRequest){
        Temat temat = Temat.builder()
                .name(tematRequest.getName())
                .description(tematRequest.getDescription())
                .deadline(tematRequest.getDeadline())
                .isReserved(false).build();
        Temat savedTemat = tematRepository.save(temat);
        TematResponse zwrotka = new TematResponse(savedTemat);
        return zwrotka;
    }

    /**
     * Metoda odszukująca wszystkie niezarezerwowane tematy ({@link Temat}) z bazy danych.
     * Uzyskuje listę tematów za pomocą {@link TematRepository} i traktując ją jako strumień ({@link java.util.stream.Stream}),
     * filtruje każdy jego element na podstawie wartości pola isReserved obiektu {@link Temat}. Po przefiltrowaniu ponownie zbiera
     * elementy strumienia do postaci listy.
     * Odnalezione tematy zostają wykorzystane do utworzenia listy obiektów zwrotnych dla klasy {@link Temat} - {@link TematResponse}.
     * Zwraca listę obiektów zwrotnych typu {@link TematResponse}.
     * @return {@link List<TematResponse>}
     */
    public List<TematResponse> getAllWolneTematy(){
        List<Temat> tematy =tematRepository.findAll().stream().filter(not(Temat::getIsReserved)).collect(Collectors.toList());
        List<TematResponse> wynikFunkcji = new ArrayList<>();
        for (Temat temat: tematy) {
            wynikFunkcji.add(new TematResponse(temat));
        }
        return wynikFunkcji;
    }

    /**
     * Metoda aktualizująca dane tematu ({@link Temat}) w bazie danych. Odszukuje
     * temat na podstawie jego Id ({@link Long}) używając do tego {@link TematRepository}.
     * Pola odszukanego tematu zostają zaktualizowane na podstawie danych przekazanych
     * w obiekcie pomocniczym typu {@link TematRequest}. Zmodyfikowany temat zostaje
     * zapisany za pomocą {@link TematRepository}, a jego zapisana postać zwrócona
     * w postaci obiektu zwrotnego typu {@link TematResponse}
     * @param id Id modyfikowanego tematu (typu {@link Long})
     * @param tematRequest Obiekt pomocniczy typu {@link TematRequest} przechowujący nowe dane dla modyfikowanego tematu
     * @return {@link TematResponse}
     */
    public TematResponse updateTemat(Long id,TematRequest tematRequest) {
        Temat temat = tematRepository.findById(id).get();
        temat.setName(tematRequest.getName());
        temat.setDescription(tematRequest.getDescription());
        temat.setIsReserved(tematRequest.getIsReserved().get());
        temat.setDeadline(tematRequest.getDeadline());
        Temat savedTemat = tematRepository.save(temat);
        return new TematResponse(savedTemat);
    }

    /**
     * Metoda aktualizująca pole deadline tematu ({@link Temat}) w bazie danych. Odszukuje
     * temat na podstawie jego Id ({@link Long}) używając do tego {@link TematRepository}.
     * Następnie pole deadline znalezionego tematu zostaje zastąpione wartością przekazaną w parametrze.
     * Zmodyfikowany temat zostaje zapisany za pomocą {@link TematRepository}, a jego zapisana postać zwrócona
     * w postaci obiektu zwrotnego typu {@link TematResponse}
     * @param id Id modyfikowanego tematu (typu {@link Long})
     * @param deadline Data i czas zapisane jako {@link Timestamp}
     * @return {@link TematResponse}
     */
    public TematResponse updateTematDeadline(Long id,Timestamp deadline) {
        Temat temat = tematRepository.findById(id).get();
        temat.setDeadline(deadline);
        Temat savedTemat = tematRepository.save(temat);
        return new TematResponse(savedTemat);
    }

    /**
     * Metoda usuwająca temat o podanym Id. Odszukuje odpowiedni temat
     * używając {@link TematRepository} i usuwa ten temat używając metody delete.
     * @param id Id tematu typu {@link Long}
     */
    public void deleteTemat(Long id) {
        Temat temat = tematRepository.findById(id).get();
        tematRepository.delete(temat);
    }

    /**
     * Metoda rezerwująca {@link Temat} o podanym Id ({@link Long}) dla studenta ({@link com.Adam.Lucja.JavaPRO.Entity.Student})
     * o podanym studentId ({@link Long}). Temat zostaje odszukany na podstawie Id poprzez {@link FileRepository},
     * następnie wartość jego pola isReserved zostaje ustawiona na wartość true.
     * Kolejnie zostaje utworzony obiekt pomocniczy (projektRequest) typu {@link ProjektRequest}, który zostaje utworzony korzystając
     * z buildera (Lombok) na podstawie id tematu, id studenta i deadline zapisanego w temacie.
     * Następnie wykorzystując {@link ProjektService} następuje próba odszukania już utworzonego projektu ({@link Projekt}) dla
     * tego tematu. Jeśli się powiedzie, student o podanym id zostanie dopisany do projektu, z wykorzystaniem
     * stworzonego wcześniej obiektu pomocniczego . Jeśli próba odszukania się nie powiodła, zostanie utworzony
     * nowy projekt na podstawie danych zawartych w obiekcie pomocniczym. Tworzeniem projektu i dołączaniem do niego
     * studenta zajmuje się {@link ProjektService}. Finalnie rezerwowany temat zostaje zapisany w bazie danych
     * z użyciem {@link TematRepository}, a reprezentacja jego zapisu zwrócona w postaci obiektu zwrotnego
     * typu {@link TematResponse}.
     * @param id Id rezerwowanego tematu typu {@link Long}
     * @param studentId Id studenta rezerwującego temat, typ {@link Long}
     * @return {@link Temat}
     */
    public TematResponse rezerwujTemat(Long id, Long studentId) {
        Temat temat = tematRepository.findById(id).get();
        temat.setIsReserved(true);
        ProjektRequest projektRequest = ProjektRequest.builder()
                .tematId(temat.getId())
                .studentId(studentId)
//                .deadline(Timestamp.from(Instant.now().plusSeconds(7889232)))
                .deadline(temat.getDeadline())
                .build();
        try {
            projektService.getOneProjektByTematId(id);
            projektService.addStudentToProject(projektRequest);
        }catch (NoSuchElementException e) {
            projektService.createProjekt(projektRequest);
        }
        Temat savedTemat = tematRepository.save(temat);
        return new TematResponse(savedTemat);
    }
}
