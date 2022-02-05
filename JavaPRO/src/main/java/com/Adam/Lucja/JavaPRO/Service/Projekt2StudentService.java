package com.Adam.Lucja.JavaPRO.Service;

import com.Adam.Lucja.JavaPRO.DTO.Response.ProjektResponse;
import com.Adam.Lucja.JavaPRO.DTO.Response.StudentResponse;
import com.Adam.Lucja.JavaPRO.DTO.Response.TematResponse;
import com.Adam.Lucja.JavaPRO.Entity.Projekt;
import com.Adam.Lucja.JavaPRO.Entity.Projekt2Student;
import com.Adam.Lucja.JavaPRO.Entity.Student;
import com.Adam.Lucja.JavaPRO.Repository.Projekt2StudentRepository;
import com.Adam.Lucja.JavaPRO.Repository.ProjektRepository;
import com.Adam.Lucja.JavaPRO.Repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * Klasa serwisowa do obiektu {@link Projekt2Student}, wiążącego {@link Projekt} ze {@link Student}em
 */
@Service
@Transactional //adnotacja zmieniająca tryb komunikacji z bazą danych, dzięki czemu można wybierać duże dane (np. pliki z bazy)
public class Projekt2StudentService {

    @Autowired
    private Projekt2StudentRepository projekt2StudentRepository;

    /**
     * Metoda odszukująca {@link List}ę obiektów wiążących {@link Projekt2Student} za pomocą {@link Projekt2StudentRepository}
     * na podstawie przekazanego id{@link Student}a. Na podstawie tej listy, tworzona jest lista obiektów zwrotnych typu
     * {@link ProjektResponse} będąca później wynikiem funkcji. Dla każdego <a style="color:orange;">elementu</a> listy obiektów wiążących tworzony jest nowy obiekt zwrotny typu
     * {@link ProjektResponse}, na podstawie {@link Projekt}u uzyskanego z obiektu wiążącego będącego aktualnie używanym
     * <a style="color:orange;">elementem</a> pętli. Nowo utworzonemu obiektowi zwrotnemu przypisana zostaje lista obiektów
     * zwrotnych typu {@link StudentResponse} uzyskana w wyniku działania metody getStudenciByProjektId serwisu {@link Projekt2StudentService}.
     * Następnie obiekt zwrotny zostaje przypisanej do listy wynikowej funkcji, a po sprawdzeniu wszystkich elementów wiążących
     * następuje zwrócenie listy wynikowej.
     * @param idStudenta Id studenta ({@link Long}), dla którego szukamy powiązań
     * @return {@link List<ProjektResponse>}
     */
    public List<ProjektResponse> getProjektByStudentId(Long idStudenta){
        List<Projekt2Student> listaProjektow2Student = projekt2StudentRepository.findAllByStudentId(idStudenta);
        List<ProjektResponse> wynikFunkcji = new ArrayList<>();
        for(Projekt2Student projekt2Student: listaProjektow2Student){
            ProjektResponse temp =new ProjektResponse(projekt2Student.getProjekt());
            temp.setStudenci(getStudenciByProjektId(temp.getId()));
            wynikFunkcji.add(temp);
        }
        return wynikFunkcji;
    }
    /**
     * Metoda odszukująca {@link List}ę obiektów wiążących {@link Projekt2Student} za pomocą {@link Projekt2StudentRepository}
     * na podstawie przekazanego id{@link Projekt}u. Na podstawie tej listy, tworzona jest lista obiektów zwrotnych typu
     * {@link StudentResponse} będąca później wynikiem funkcji. Dla każdego <p style="color:blue;">elementu</p> listy obiektów wiążących tworzony jest nowy obiekt zwrotny typu
     * {@link StudentResponse}, na podstawie {@link Student}a uzyskanego z obiektu wiążącego będącego aktualnie używanym
     * <p style="color:blue;">elementem</p> pętli. Nowo utworzony obiekt zostaje dodany do listy wynikowej.
     * Po sprawdzeniu wszystkich elementów wiążących następuje zwrócenie listy wynikowej.
     * @param idProjektu Id projektu ({@link Long}), dla którego szukamy powiązań
     * @return {@link List<StudentResponse>}
     */
    public List<StudentResponse> getStudenciByProjektId(Long idProjektu){
        List<Projekt2Student> listaProjektow2Student = projekt2StudentRepository.findAllByProjektId(idProjektu);
        List<StudentResponse> wynikFunkcji = new ArrayList<>();
        for(Projekt2Student projekt2Student: listaProjektow2Student){
            wynikFunkcji.add(new StudentResponse(projekt2Student.getStudent()));
        }
        return wynikFunkcji;
    }

}
