package com.Adam.Lucja.JavaPRO.Controller;

import com.Adam.Lucja.JavaPRO.DTO.Request.AuthRequest;
import com.Adam.Lucja.JavaPRO.DTO.Request.StudentRequest;
import com.Adam.Lucja.JavaPRO.DTO.Response.AuthResponse;
import com.Adam.Lucja.JavaPRO.DTO.Response.ProjektResponse;
import com.Adam.Lucja.JavaPRO.DTO.Response.StudentResponse;
import com.Adam.Lucja.JavaPRO.DTO.Response.TematResponse;
import com.Adam.Lucja.JavaPRO.Entity.Projekt2Student;
import com.Adam.Lucja.JavaPRO.Entity.Student;
import com.Adam.Lucja.JavaPRO.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collections;
import java.util.List;

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
    Projekt2StudentService projekt2StudentServiceService;

    @RequestMapping({"/","/index"})
    String index(Model model) {
        List<TematResponse> tematy = tematService.getAllTematy();
        Collections.sort(tematy);
        model.addAttribute("tematy",tematy);
        return "index";
    }

    @PostMapping("/login")
    String loginDetails(@RequestBody MultiValueMap<String, String> formData) {
        AuthRequest authRequest = new AuthRequest(formData.get("username").get(0),formData.get("password").get(0));
        AuthResponse response = authService.getAuthToken(authRequest);
        return "login";
    }
    @GetMapping("/login")
    String login() {
        return "login";
    }

    @GetMapping("/logout")
    String logout() {
        return "logout";
    }

    @GetMapping("/account")
    String account(Model model, Principal principal) {
        String nrAlbumu = principal.getName();
        Student student = studentService.getStudentByNrAlbumu(nrAlbumu);
        List<ProjektResponse> projektyStudenta = projekt2StudentServiceService.getProjektByStudentId(student.getId());
        model.addAttribute("projekty", projektyStudenta);
        return "account";
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
//
    @RequestMapping("/register")
    String register(@RequestBody MultiValueMap<String, String> formData, Model model){
        StudentRequest studentRequest = new StudentRequest(
                formData.get("name").get(0),
                formData.get("surname").get(0),
                formData.get("email").get(0),
                formData.get("nrAlbum").get(0),
                formData.get("password").get(0));
        String matchingPassword = formData.get("password2").get(0);
        if(studentRequest.getPassword()==matchingPassword){
            studentService.createStudent(studentRequest);
            model.addAttribute("registerSuccess");
        }
        else{
            model.addAttribute("passwordDontMatch");
        }
        return "register";
    }
}