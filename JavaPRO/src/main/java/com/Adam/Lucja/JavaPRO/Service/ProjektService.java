package com.Adam.Lucja.JavaPRO.Service;

import com.Adam.Lucja.JavaPRO.DTO.Request.ProjektRequest;
import com.Adam.Lucja.JavaPRO.DTO.Request.StudentRequest;
import com.Adam.Lucja.JavaPRO.DTO.Response.MessageResponse;
import com.Adam.Lucja.JavaPRO.DTO.Response.ProjektResponse;
import com.Adam.Lucja.JavaPRO.DTO.Response.StudentResponse;
import com.Adam.Lucja.JavaPRO.Entity.File;
import com.Adam.Lucja.JavaPRO.Entity.Projekt;
import com.Adam.Lucja.JavaPRO.Entity.Projekt2Student;
import com.Adam.Lucja.JavaPRO.Entity.Student;
import com.Adam.Lucja.JavaPRO.Repository.Projekt2StudentRepository;
import com.Adam.Lucja.JavaPRO.Repository.ProjektRepository;
import com.Adam.Lucja.JavaPRO.Repository.StudentRepository;
import com.Adam.Lucja.JavaPRO.Repository.TematRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.stream.Stream;
import com.Adam.Lucja.JavaPRO.Entity.Temat;

import javax.transaction.Transactional;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Klasa serwisowa do obsługi Projektów ({@link Projekt})
 */
@Service
@Transactional //adnotacja zmieniająca tryb komunikacji z bazą danych, dzięki czemu można wybierać duże dane (np. pliki z bazy)
public class ProjektService {
    @Autowired
    private ProjektRepository projektRepository;

    @Autowired
    private TematRepository tematRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private Projekt2StudentRepository projekt2StudentRepository;

    @Autowired
    private FileService fileService;

    /**
     * Metoda odszukująca wszystkie {@link Projekt}y w bazie danych za pomocą {@link ProjektRepository}.
     * Lista odnalezionych projektów zostaje potraktowana jako strumień ({@link java.util.stream.Stream}).
     * Elementy znajdujące się w tym strumieniu zostają zmapowane do obiektów zwrotnych typu {@link ProjektResponse},
     * a następnie ponownie zebrane do postaci listy ({@link ArrayList}).
     * W efekcie otrzymujemy {@link List<ProjektResponse>}ę wypełnioną obiektami zwrotnymi.
     * @return {@link List<ProjektResponse>}
     */
    public List<ProjektResponse> getAllProjekty(){
        return projektRepository.findAll().stream()
                .map(ProjektResponse::new)
                .collect(Collectors.toList());
    }
    /**
     * Metoda odszukująca {@link Projekt} o podanym id ({@link Long}) w bazie za pomocą {@link ProjektRepository}.
     * Na podstawie odszukanego projektu zostaje utworzony obiekt zwrotny klasy {@link ProjektResponse}.
     * @param id Id szukanego projektu ({@link Long})
     * @return {@link ProjektResponse}
     */
    public ProjektResponse getProjekt(Long id){
        Projekt projekt = projektRepository.getById(id).get();
        return new ProjektResponse(projekt);
    }
    /**
     * Metoda odszukująca {@link Projekt} o podanym id{@link com.Adam.Lucja.JavaPRO.Entity.Temat}u ({@link Long})
     * w bazie za pomocą {@link ProjektRepository}. Zwraca obiekt odszukanego projektu.
     * @param id Id szukanego tematu ({@link Long})
     * @return {@link Projekt}
     */
    public Projekt getOneProjektByTematId(Long id){
        Projekt projekt = projektRepository.getByTematId(id).get();
        return projekt;
//        return new ProjektResponse(projekt);
    }
    /**
     * Metoda odszukująca {@link List}ę {@link Projekt}ów o podanym id{@link com.Adam.Lucja.JavaPRO.Entity.Temat}u ({@link Long})
     * w bazie za pomocą {@link ProjektRepository}. Otrzymana lista zostaje potraktowana jako strumień ({@link Stream}),
     * jego elementy zmapowane do postaci obiektów zwrotnych typu {@link ProjektResponse}, a następnie ponownie
     * zebrane do postaci listy i tak przekazane jako wynik funkcji.
     * @param id Id szukanego tematu ({@link Long})
     * @return {@link List<ProjektResponse>}
     */
    public List<ProjektResponse> getProjektyByTematId(Long id){
        List<Projekt> projekt = projektRepository.findAllByTematId(id);
        return projekt.stream().map(ProjektResponse::new).collect(Collectors.toList());
//        return new ProjektResponse(projekt);
    }

    /**
     * Metoda tworzy nowy {@link Projekt} na podstawie danych przekazanych przy pomocy obiektu pomocniczego {@link ProjektRequest}
     * Jeżeli w obiekcie pomocniczym znajdowało się id{@link Student}a, student ten zostaje odszukany przez {@link StudentRepository}
     * a następnie utworzony zostaje nowy obiekt klasy {@link Projekt2Student}, wiążący studenta z projektem. Obiekt ten,
     * jaki i nowo utworzony projekt zostają zapisane w bazie danych za pomocą swoich repozytoriów ({@link ProjektRepository}, {@link Projekt2StudentRepository}).
     * W wyniku funkcji zwracana jest reprezentacja zapisu projektu w postaci obiektu typu {@link ProjektResponse}.
     * @param projektRequest Obiekt pomocniczy przechowujący dane potrzebne do utworzenia {@link Projekt}u
     * @return {@link ProjektResponse}
     */
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
            Projekt2Student savedProjekt2Student = projekt2StudentRepository.save(projekt2Student);
            savedProjekt = projektRepository.save(projekt);
        }else
            savedProjekt = projektRepository.save(projekt);

        ProjektResponse zwrotka = new ProjektResponse(savedProjekt);
        return zwrotka;
    }

    /**
     * Metoda aktualizująca dane {@link Projekt}u o podanym Id ({@link Long}) na podstawie danych
     * przekazanych przez obiekt pomocniczy {@link ProjektRequest}. Projekt zostaje odszukany w bazie danych
     * na podstawie Id przez {@link ProjektRepository}. Dane otrzymanego projektu zostają zastąpione danymi przekazanymi
     * w obiekcie pomocniczym, a następnie zmodyfikowany projekt zostaje zapisany przez {@link ProjektRepository}.
     * Reprezentacja zapisanego obiektu zostaje przekazana w postaci {@link ProjektResponse} jako wynik funkcji.
     * @param projektRequest Obiekt pomocniczy z danymi potrzebnymi do utworzenia {@link Projekt}u
     * @param id Id edytowanego projektu({@link Long})
     * @return {@link ProjektResponse}
     */
    public ProjektResponse updateProjekt (ProjektRequest projektRequest, Long id){
        Projekt projekt = projektRepository.getById(id).get();
        projekt.setSubmissionDate(projektRequest.getSubmissionDate());
        projekt.setDeadline(projektRequest.getDeadline());
        projekt.setMark(projektRequest.getMark());

        Projekt savedProjekt = projektRepository.save(projekt);
        return new ProjektResponse(savedProjekt);
    }

    /**
     * Metoda usuwająca {@link Projekt} o podanym Id ({@link Long}) z bazy danych.
     * Projekt zostaje odszukany przez {@link ProjektRepository} na podstawie przekazanego Id, a następnie
     * usunięty za pomocą metody delete.
     * @param id Id usuwanego {@link Projekt}u, typ {@link Long}
     */
    public void deleteProjekt(Long id){
        Projekt projekt = projektRepository.getById(id).get();
        projektRepository.delete(projekt);
    }

    /**
     * Metoda zapisująca przekazany plik ({@link MultipartFile}) do bazy danych za pomocą {@link FileService}, wiążąc go
     * także z {@link Projekt}em o podanym Id ({@link Long}). Po zapisaniu pliku  przez {@link FileService}, odszukany zostaje
     * projekt o podanym Id za pomocą {@link ProjektRepository}, następnie przypisany zostaje mu plik,
     * a także aktualna data i czas w postaci {@link Timestamp}. Tak zmieniony projekt zostaje zapisany w bazie danych
     * za pomocą {@link ProjektRepository}, a reprezentacja zapisu przekazana w postaci {@link ProjektResponse} w wyniku funkcji.
     * @param id Id {@link Projekt}u, dla którego przesyłany jest plik ({@link MultipartFile})
     * @param file Przesyłany plik w postaci ({@link MultipartFile})
     * @return {@link ProjektResponse}
     */
    public ProjektResponse uploadFile(Long id, MultipartFile file){
        try {
            File savedFile = fileService.saveFile(file);
            Projekt projekt = projektRepository.getById(id).get();
            projekt.setFile(savedFile);
            projekt.setSubmissionDate(Timestamp.from(Instant.now()));
            Projekt savedProjekt = projektRepository.save(projekt);
            return new ProjektResponse(savedProjekt);
        } catch (IOException e){
            e.printStackTrace();
            return new ProjektResponse();
        }
    }

    /**
     * Metoda odnajdująca plik ({@link File}) na podstawie {@link Projekt}u o przekazanym Id ({@link Long}).
     * Szukany projekt zostaje odnaleziony przez {@link ProjektRepository}, a następnie w zależności od
     * wartości pola file, zwracany jest plik przypisany do tego projektu lub nowy obiekt typu
     * {@link MessageResponse} z odpowiednią wiadomością. Z uwagi na różne typy zwracanych obiektów ({@link File} lub {@link MessageResponse})
     * w deklaracji nie sprecyzowano zwracanego typu, a zamiast tego użyto typu {@link Object}.
     * @param id Id {@link Projekt}u, dla którego szukamy pliku ({@link File})
     * @return {@link Object}
     */
    public Object getFile(Long id) {
        Projekt projekt = projektRepository.getById(id).get();
        if(projekt.getFile()==null)
            return new MessageResponse("Nie znaleziono plików dla tego projektu");
        return fileService.getFile(projekt.getFile().getId());
    }

    /**
     * Metoda oceniająca {@link Projekt} o podanym Id ({@link Long}) na ocenę podaną w parametrze mark ({@link Double}).
     * Projekt zostaje odszukany przez {@link ProjektRepository}, następnie jeśli pole SubmissionDate nie ma wartości,
     * zostaje uzupełnione aktualną datą i czasem w postaci {@link Timestamp} - na wypadek, jeśli projekt został oceniony
     * bez uprzedniego przesłania pliku. Następnie ustawiona zostaje ocena projektu. Tak zmodyfikowany projekt zostaje
     * zapisany przez {@link ProjektRepository}, a reprezentacja tego zapisu zwrócona w postaci {@link ProjektResponse}.
     * @param id Id ocenianego projektu, typ {@link Long}
     * @param mark Ocena na jaką oceniany jest projekt, typ {@link Double}
     * @return {@link ProjektResponse}
     */
    public ProjektResponse gradeProject(Long id, Double mark) {
        Projekt projekt = projektRepository.getById(id).get();
        if(projekt.getSubmissionDate()==null)
            projekt.setSubmissionDate(Timestamp.from(Instant.now()));
        projekt.setMark(mark);
        Projekt savedProjekt = projektRepository.save(projekt);
        return new ProjektResponse(savedProjekt);
    }

    /**
     * Metoda dopisująca {@link Student}a do {@link Projekt}u na podstawie danych przekaznych w obiekcie pomocniczym {@link ProjektRequest}.
     * Odszukuje obiekty studenta oraz projektu za pomocą odpowiednich repozytoriów ({@link ProjektRepository} i {@link StudentRepository})
     * używając do tego idProjektu oraz idStudenta znajdujących się w obiekcie pomocniczym. Na podstawie odszukanych
     * obiektów tworzony jest nowy obiekt klasy {@link Projekt2Student} wiążący projekt i studenta ze sobą.
     * Obiekt ten zostaje zapisany za pomocą jego repozytorium {@link Projekt2StudentRepository}.
     * W wyniku funkcji zwracana jest reprezentacja projektu w postaci obiektu zwrotnego {@link ProjektResponse}.
     * @param projektRequest Obiekt pomocniczy przechowujący dane potrzebne do oszukania odpowiednich obiektów {@link Projekt}u i {@link Student}a
     * @return {@link ProjektResponse}
     */
    public ProjektResponse addStudentToProject(ProjektRequest projektRequest) {
        Projekt projekt = projektRepository.getByTematId(projektRequest.getTematId()).get();
        Student student = studentRepository.findById(projektRequest.getStudentId()).get();
        Projekt2Student projekt2Student = Projekt2Student.builder()
                .student(student)
                .projekt(projekt).build();
        projekt2StudentRepository.save(projekt2Student);
        return new ProjektResponse(projekt);
    }
}
