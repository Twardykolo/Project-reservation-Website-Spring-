package com.Adam.Lucja.JavaPRO.Controller;

import com.Adam.Lucja.JavaPRO.DTO.Request.StudentRequest;
import com.Adam.Lucja.JavaPRO.DTO.Request.TematRequest;
import com.Adam.Lucja.JavaPRO.DTO.Response.ProjektResponse;
import com.Adam.Lucja.JavaPRO.DTO.Response.StudentResponse;
import com.Adam.Lucja.JavaPRO.DTO.Response.TematResponse;
import com.Adam.Lucja.JavaPRO.Entity.Student;
import com.Adam.Lucja.JavaPRO.Service.*;
import lombok.SneakyThrows;
import org.apache.tomcat.util.descriptor.web.ErrorPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;

import java.security.Principal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

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

    @RequestMapping({"/","/index"})
    String index(Model model, Principal principal) {
        List<Long> tematyStudenta = new ArrayList<>();
        try {
            String nrAlbumu = principal.getName();
            Student student = studentService.getStudentByNrAlbumu(nrAlbumu);
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

    @RequestMapping("/login")
    String login() {
        return "login";
    }

    @GetMapping("/logout")
    String logout() {
        return "logout";
    }

    @GetMapping("/account")
    String account(Model model, Principal principal) {
        if(!checkIfLoggedInAsUser(model,principal))
            return index(model,principal);

        String nrAlbumu = principal.getName();
        Student student = studentService.getStudentByNrAlbumu(nrAlbumu);
        List<ProjektResponse> projektyStudenta = projekt2StudentService.getProjektByStudentId(student.getId());
        model.addAttribute("projekty", projektyStudenta);
        return "account";
    }

    @RequestMapping("/uploadFile/{id}")
    String uploadFile(Model model, Principal principal, @PathVariable Long id, @RequestParam("file") MultipartFile file) {
        if(!checkIfLoggedInAsUser(model,principal))
            return index(model,principal);
        projektService.uploadFile(id,file);
        return account(model,principal);
    }

    @GetMapping("/adminPanel")
    String adminPanel(Model model, Principal principal) {
        //sprawdzenie czy jest adminem, jak nie to przekierowanie na index
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication.getAuthorities().stream().anyMatch(
                r-> r.getAuthority().equals("ROLE_ADMIN"));
        if(!isAdmin)
            return index(model,principal);

        List<TematResponse> wolneTematy = tematService.getAllWolneTematy();
        model.addAttribute("wolneTematy",wolneTematy);

        List<ProjektResponse> projekty = projektService.getAllProjekty();
        for(ProjektResponse projekt : projekty){
            List<StudentResponse> studenci = projekt2StudentService.getStudenciByProjektId(projekt.getId());
            projekt.setStudenci(studenci);
        }
        model.addAttribute("projekty",projekty);
        return "adminPanel";
    }

    @GetMapping("/register")
    String register() {
        return "register";
    }
    @GetMapping("/loginZepsuty")
    String loginFailed(Model model){
        model.addAttribute("loginError", true);
        return "login";
    }

//    @GetMapping("/specialController")
//    String specialController(){
//        return "tak";
//    }

    @RequestMapping("/register")
    String register(@RequestBody MultiValueMap<String, String> formData, Model model){
        StudentRequest studentRequest = new StudentRequest(
                formData.get("name").get(0),
                formData.get("surname").get(0),
                formData.get("password").get(0),
                formData.get("email").get(0),
                formData.get("nrAlbum").get(0));
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
        return "register";
    }

    @RequestMapping("/zarezerwujTemat/{id}")
    String reserve(Model model,Principal principal, @PathVariable Long id){
        if(!checkIfLoggedInAsUser(model,principal))
            return index(model,principal);
        String nrAlbumu = principal.getName();
        Student student = studentService.getStudentByNrAlbumu(nrAlbumu);
        tematService.rezerwujTemat(id, student.getId());
        return index(model,principal);
    }
    @RequestMapping("/dodajTemat")
    @SneakyThrows
    String addTemat(Model model,Principal principal, @RequestBody MultiValueMap<String, String> formData){
        if(!checkIfLoggedInAsAdmin(model,principal))
            return index(model,principal);
//        System.out.println(formData.get("deadline").get(0));
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date d = formatter.parse(formData.get("deadline").get(0));
        TematRequest request = new TematRequest(formData.get("name").get(0),
                                                formData.get("description").get(0),
                                                new Timestamp(d.getTime()),
                                                Optional.of(false));
        tematService.createTemat(request);
        return adminPanel(model,principal);
    }

    @RequestMapping("/projekty/grade/{id}")
    String gradeProject(Model model,Principal principal,@PathVariable("id") Long projektId,@RequestParam("mark") String formData){
        if(!checkIfLoggedInAsAdmin(model,principal))
            return index(model,principal);
        Double mark = Double.parseDouble(formData);
        projektService.gradeProject(projektId,mark);
        return adminPanel(model,principal);
    }

    Boolean checkIfLoggedInAsUser(Model model,Principal principal){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isUser = authentication.getAuthorities().stream().anyMatch(
                r-> r.getAuthority().equals("ROLE_USER"));
        return isUser;
    }
    Boolean checkIfLoggedInAsAdmin(Model model,Principal principal){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication.getAuthorities().stream().anyMatch(
                r-> r.getAuthority().equals("ROLE_ADMIN"));
        return isAdmin;
    }
}