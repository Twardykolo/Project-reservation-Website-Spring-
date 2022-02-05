package com.Adam.Lucja.JavaPRO.Controller;

import com.Adam.Lucja.JavaPRO.DTO.Request.StudentRequest;
import com.Adam.Lucja.JavaPRO.DTO.Request.TematRequest;
import com.Adam.Lucja.JavaPRO.DTO.Response.ProjektResponse;
import com.Adam.Lucja.JavaPRO.DTO.Response.StudentResponse;
import com.Adam.Lucja.JavaPRO.DTO.Response.TematResponse;
import com.Adam.Lucja.JavaPRO.Entity.Student;
import com.Adam.Lucja.JavaPRO.Service.*;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.Adam.Lucja.JavaPRO.Entity.Temat;

import java.security.Principal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Kontroller odpowiedzialny za interfejs webowy.
 */
@Controller
class WebMvcController {

    @ModelAttribute("module")
    String module() {
        return "home";
    }

    @Autowired
    AuthService authService;

    @Autowired
    TematService tematService;

    @Autowired
    StudentService studentService;

    @Autowired
    ProjektService projektService;

    @Autowired
    Projekt2StudentService projekt2StudentService;

    /**
     * Metoda wyświetlająca zawartość index.html, zmapowana dla adresów ścieżek / i /index (np. localhost:8080/index).
     * Dane potrzebne do działania zostają przekazane poprzez metodę addAttribute obiektu {@link Model}.
     * Przekazane tak atrybuty są dostępne do użycia z poziomu Thymeleaf ( th:... w plikach .html).
     * @param model Obiekt przeznaczony do przechowywania atrybutów
     * @param principal Obiekt reprezentujący zalogowanego użytkownika
     * @return Wyświetla wypełniony index.html
     */
    @RequestMapping({"/","/index"})
    String index(Model model, Principal principal) {
        List<Long> tematyStudenta = new ArrayList<>();
        try {
            String nrAlbumu = principal.getName();
            model.addAttribute("login",nrAlbumu);
            Student student = studentService.getStudentByNrAlbumu(nrAlbumu);
            model.addAttribute("imie",student.getName());
            model.addAttribute("nazwisko",student.getSurname());
            List<ProjektResponse> projektyStudenta = projekt2StudentService.getProjektByStudentId(student.getId());
            for (ProjektResponse projekt : projektyStudenta) {
                tematyStudenta.add(tematService.getTemat(projekt.getTemat().getId()).getId());
            }
        }catch (Exception e){
        }
        List<TematResponse> tematy = tematService.getAllAviableTematy();
        for (TematResponse temat:  tematy) {
            List<ProjektResponse> projekty = projektService.getProjektyByTematId(temat.getId());
            temat.setLiczbaOsob(projekty.size());
        }
        Collections.sort(tematy);
        model.addAttribute("tematy",tematy);
        model.addAttribute("tematyStudenta",tematyStudenta);
        return "index";
    }

    /**
     * Metoda wyświetlająca login.html, zmapowana dla ścieżki /login
     * @return Wyświetla login.html
     */
    @RequestMapping("/login")
    String login(Model model) {
        return "login";
    }

    /**
     * Metoda wyświetlająca panel użytkownika, zmapowana dla ścieżki /account.
     * Jeśli aktualnie zalogowany użytkownik nie ma uprawnień użytkownika lub ma
     * uprawnienia administratora, zostanie mu wyświetlony index.html przy użyciu metody index.
     * Jeśli zalogowany użytkownik ma uprawnienia użytkownika, za pomocą {@link Model}u przekazane zostają dane potrzebne
     * do wyświetlenia panelu konta, a następnie wyświetlony zostaje account.html
     * @param model
     * @param principal
     * @return Wyświetla account.html lub index.html
     */
    @GetMapping("/account")
    String account(Model model, Principal principal) {
        if(!checkIfUser(model,principal) || checkIfAdmin(model,principal))
            return index(model,principal);

        String nrAlbumu = principal.getName();
        Student student = studentService.getStudentByNrAlbumu(nrAlbumu);
        List<ProjektResponse> projektyStudenta = projekt2StudentService.getProjektByStudentId(student.getId());
        model.addAttribute("login",nrAlbumu);
        model.addAttribute("imie",student.getName());
        model.addAttribute("nazwisko",student.getSurname());
        model.addAttribute("projekty", projektyStudenta);
        return "account";
    }

    /**
     * Metoda obsługująca przekazywany za pomocą formularza plik. Zmapowana została na /uploadFile/, a następujące po
     * tym fragmencie znaki, traktowane są jako wartość zmiennej id ({@link PathVariable}). Plik zostaje przekazany
     * jako jedyny parametr formularza i odnaleziony przy pomocy nazwy "file" ujętej w adnotacji {@link RequestParam}.
     * Plik przekazany zostaje w postaci {@link MultipartFile}. Jeśli metoda została wywołana przez użytkownika
     * o uprawnieniach użytkownika, plik zostanie przekazany do metody uploadFile klasy {@link ProjektService} oraz
     * wyświetlony zostanie panel konta za pomocą metody account. Jeżeli użytkownik nie posiada uprawnień użytkownika,
     * zamiast załadowania pliku, zostanie wyświetlony index.html przy pomocy metody index.
     * @param model
     * @param principal
     * @param id
     * @param file
     * @return Wyświetla account.html lub index.html
     */
    @RequestMapping("/uploadFile/{id}")
    String uploadFile(Model model, Principal principal, @PathVariable Long id, @RequestParam("file") MultipartFile file) {
        if(!checkIfUser(model,principal))
            return index(model,principal);
        projektService.uploadFile(id,file);
        return account(model,principal);
    }

    /**
     * Metoda wyświetlajaca panel administratora (prowadzącego), zmapowana dla ścieżki /adminPanel.
     * Jeżeli aktualnie zalogowany użytkownik nie ma uprawnień administratora, zostanie mu wyświetlony
     * index.html za pomocą metody index. Jeżeli użytkownik posiada uprawnienia administratora, zebrane
     * zostają dane potrzebne do wyświetlenia panelu administratora i przekazane jako atrybuty {@link Model}u.
     * W wyniku wyświetlony zostaje adminPanel.html
     * @param model
     * @param principal
     * @return Wyświetla adminPanel.html lub index.html
     */
    @GetMapping("/adminPanel")
    String adminPanel(Model model, Principal principal) {
        //sprawdzenie czy jest adminem, jak nie to przekierowanie na index
        if(!checkIfAdmin(model,principal))
            return index(model,principal);

        List<TematResponse> wolneTematy = tematService.getAllWolneTematy();
        model.addAttribute("wolneTematy",wolneTematy);
        String login = principal.getName();
        model.addAttribute("login",login);

        List<ProjektResponse> projekty = projektService.getAllProjekty();
        for(ProjektResponse projekt : projekty){
            List<StudentResponse> studenci = projekt2StudentService.getStudenciByProjektId(projekt.getId());
            projekt.setStudenci(studenci);
        }
        model.addAttribute("projekty",projekty);
        return "adminPanel";
    }

    /**
     * Metoda zmapowana dla ścieżki /register, wyświetla register.html
     * @return Wyświetla register.html
     */
    @GetMapping("/register")
    String register(Model model) {
        return "register";
    }

    /**
     * Metoda zmapowana dla ścieżki /loginZepsuty. Dociera tu przekierowanie wywoływane przez SpringSecurity
     * w przypadku, gdy podane zostaną błędne dane logowania. Metoda dodaje pojedynczy atrybut świadczący o
     * błędzie logowania, po czym wyświetla login.html za pomocą metody login.
     * @param model
     * @return Wyświetla login.html
     */
    @GetMapping("/loginZepsuty")
    String loginFailed(Model model){
        model.addAttribute("loginError", true);
        return login(model);
    }

    /**
     * Metoda zmapowana dla ścieżki /register, przetwarzająca dane podane w formularzu rejestracyjnym.
     * Dane odczytane z formularza zostają użyte do utworzenia nowego obiektu {@link StudentRequest}.
     * Jeżeli hasła odczytane z formularza się nie zgadzały (hasło i potwierdzenie hasła), dodany zostaje
     * odpowiedni atrybut {@link Model}u.
     * Jeżeli hasła był zgodne, następuje próba utworzenia nowego studenta {@link Student} za pomocą
     * klasy {@link StudentService}. W zależności od powodzenia rejestracji, ustawiony zostaje odpowiedni atrybut {@link Model}u.
     * Ostatecznie w obu przypadkach wyświetlony zostaje register.html przy pomocy metody register.
     * @param formData
     * @param model
     * @return Wyświetla register.html
     */
    @RequestMapping("/register")
    String register(@RequestBody MultiValueMap<String, String> formData, Model model){
        StudentRequest studentRequest = new StudentRequest(
                formData.get("name").get(0),
                formData.get("surname").get(0),
                formData.get("password").get(0),
                formData.get("email").get(0).toLowerCase(Locale.ROOT),
                formData.get("nrAlbum").get(0).toLowerCase(Locale.ROOT));
        String matchingPassword = formData.get("password2").get(0);
        if(matchingPassword.equals(studentRequest.getPassword())){
            try {
                studentService.createStudent(studentRequest);
                model.addAttribute("registerSuccess", true);
            }catch(Exception e){
                model.addAttribute("registerFailed",true);
            }
        }
        else{
            model.addAttribute("passwordNotMatch",true);
        }
        return register(model);
    }

    /**
     * Metoda zmapowana dla ścieżki /zarezerwujTemat/, wartości zapisane dalej, traktowane są jako wartość parametru id.
     * Celem metody jest obsługa rezerwacji tematów. Metoda sprawdza, czy aktualnie zalogowany użytkownik ma
     * uprawnienia użytkownika. Jeżeli ich nie ma, wyświetlony zostaje index.html za pomocą metody index.
     * Jeżeli użytkownik posiada te uprawnienia, na podstawie jego loginu pobranego z obiektu {@link Principal},
     * odszukany zostaje odpowiadający mu obiekt {@link Student} przy użyciu klasay {@link StudentService}.
     * Następnie dokonana zostaje rezerwacja {@link com.Adam.Lucja.JavaPRO.Entity.Temat}u poprzez klasę
     * {@link TematService} używając idTematu podanego jako zmienna w ścieżce ({@link PathVariable}), a także idStudenta
     * uzyskane z odnalezionego wcześniej obiektu {@link Student}.
     * Ostaetcznie wyświetlony zostaje index.html za pomocą metody index.
     * @param model
     * @param principal
     * @param id
     * @return Wyświetla index.html
     */
    @RequestMapping("/zarezerwujTemat/{id}")
    String reserve(Model model,Principal principal, @PathVariable Long id){
        if(!checkIfUser(model,principal))
            return index(model,principal);
        String nrAlbumu = principal.getName();
        Student student = studentService.getStudentByNrAlbumu(nrAlbumu);
        tematService.rezerwujTemat(id, student.getId());
        return index(model,principal);
    }

    /**
     * Metoda zmapowana dla ścieżki /dodajTemat. Służy do dodawania {@link Temat}u na podstawie danych przekazanych
     * przez formularz ({@link RequestBody}). Jeżeli aktualnie zalogowany użytkownik nie ma uprawnień administratora,
     * wyświetlony zostaje index.html przy użyciu metody index.
     * Jeżeli data końcowa (deadline) przekazana przez formularz nie następuje
     * później niż aktualna data, przypisany zostaje atrybut świadczący o niewłaściwej dacie i wyświetlony zostaje
     * adminPanel.html za pomocą metody adminPanel.
     * Jeżeli data jest poprawna, utworzony zostanie nowy {@link Temat} za pomocą klasy {@link TematService}, a następnie
     * wyświetlony zostanie adminPanel.html za pomocą metody adminPanel.
     * @param model
     * @param principal
     * @param formData
     * @return Wyświetla adminPanel.html lub index.html
     */
    @RequestMapping("/dodajTemat")
    @SneakyThrows //adnotacja lomboka pozwalająca na pominięcie obsługi rzucanych wyjątków
    String addTemat(Model model,Principal principal, @RequestBody MultiValueMap<String, String> formData){
        if(!checkIfAdmin(model,principal))
            return index(model,principal);

        //odkodowanie przekazanej w formularzu daty wg. określonego formatu zapisu
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date d = formatter.parse(formData.get("deadline").get(0));

        Date today = new Date();
        if(!d.after(today)){
            model.addAttribute("invalid-date", true);
            return adminPanel(model,principal);
        }
        TematRequest request = new TematRequest(formData.get("name").get(0),
                                                formData.get("description").get(0),
                                                new Timestamp(d.getTime()),
                                                Optional.of(false));
        tematService.createTemat(request);
        return adminPanel(model,principal);
    }

    /**
     * Metoda służąca do oceny projektu, zmapowana na ścieżce /projekty/grade/. Wartość następująca po tej ścieżce
     * traktowana jest jako parametr projektId. Wartość przypisywanej oceny zostaje odebrana jako jedyny parametr
     * przekazany przez formularz pod nazwą mark ({@link RequestParam}("mark")).
     * Jeżeli aktualnie zalogowany użytkownik nie posiada uprawnień administratora, wyświetlony zostaje index.html za
     * pomocą metody index.
     * Ocena odczytana z formularza zostaje przekonwertowana na liczbę typu {@link Double}. W przypadku, kiedy konwersja
     * nie powiodła się, dodany zostaje atrybut świadczący o błędnej ocenie, oraz wyświetlony zostaje adminPanel.html
     * przy użyciu metody adminPanel.
     * Jeżeli konwersja przebiegła pomyślnie, ocena zostaje przypisana do projektu o podanym w ścieżce id, za pomocą
     * klasy {@link ProjektService}. Na koniec wyświetlony zostaje adminPanel.html za pomocą metody adminPanel.
     * @param model
     * @param principal
     * @param projektId
     * @param formData
     * @return Wyświetla adminPanel.html lub index.html
     */
    @RequestMapping("/projekty/grade/{id}")
    String gradeProject(Model model,Principal principal,@PathVariable("id") Long projektId,@RequestParam("mark") String formData){
        if(!checkIfAdmin(model,principal))
            return index(model,principal);
        Double mark;
        if(projektId!=null) {
            try {
                mark = Double.parseDouble(formData);
            } catch (NumberFormatException e){
                model.addAttribute("bledna-ocena", true);
                return adminPanel(model,principal);
            }
            projektService.gradeProject(projektId, mark);
        }
        return adminPanel(model,principal);
    }

    /**
     * Metoda zmapowana dla ścieżki /tematy/deadline/. Wartość następująca po tej ścieżce traktowana jest jako
     * wartość zmiennej tematId. Metoda ma za zadanie zmianę aktualnie ustawionej wartości deadline
     * {@link Temat}u o podanym id.
     * Jeżeli aktualnie zalogowany użytkownik nie posiada uprawnień adminstratora, wyświetlony zostaje index.html za
     * pomocą metody index.
     * Jeżeli użytkownik posiada uprawnienia, metoda odkodowuje datę przekazaną przez formularz jako parametr ({@link RequestParam}("deadline")).
     * Jeżeli data ta nie następuje później niż data aktualna, funkcja dodaje świadczący o tym atrybut, po czym wyświetla adminPanel.html za
     * pomocą metody adminPanel.
     * Jeżeli data jest poprawna, zostaje ona przekazana wraz z idTematu do metody updateTematDeadline klasy {@link TematService}.
     * Ostatecznie wsyświetlony zostaje adminPanel.html za pomocą metody adminPanel.
     * @param model
     * @param principal
     * @param tematId
     * @param formData
     * @return Wyświetla adminPanel.html lub index.html
     */
    @RequestMapping("/tematy/deadline/{id}")
    @SneakyThrows //adnotacja lomboka pozwalająca na pominięcie obsługi rzucanych wyjątków
    String changeTematDeadline(Model model,Principal principal,@PathVariable("id") Long tematId,@RequestParam("deadline") String formData){
        if(!checkIfAdmin(model,principal))
            return index(model,principal);

        //odkodowanie przekazanej w formularzu daty wg. określonego formatu zapisu
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date d = formatter.parse(formData);

        Date today = new Date();
        if(!d.after(today)){
            model.addAttribute("invalid-date", true);
            return adminPanel(model,principal);
        }
        Timestamp deadline = new Timestamp(d.getTime());
        tematService.updateTematDeadline(tematId,deadline);
        return adminPanel(model,principal);
    }

    /**
     * Funkcja pomocnicza sprawdzająca czy aktualnie zalogowany użytkownik posiada uprawnienia użytkownika.
     * Do sprawdzania uprawnień wykorzystuje funckję lambda.
     * @param model
     * @param principal
     * @return {@link Boolean}
     */
    Boolean checkIfUser(Model model, Principal principal){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isUser = authentication.getAuthorities().stream().anyMatch(
                r-> r.getAuthority().equals("ROLE_USER"));
        return isUser;
    }
    /**
     * Funkcja pomocnicza sprawdzająca czy aktualnie zalogowany użytkownik posiada uprawnienia administratora.
     * Do sprawdzania uprawnień wykorzystuje funckję lambda.
     * @param model
     * @param principal
     * @return {@link Boolean}
     */
    Boolean checkIfAdmin(Model model, Principal principal){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication.getAuthorities().stream().anyMatch(
                r-> r.getAuthority().equals("ROLE_ADMIN"));
        return isAdmin;
    }
}