package com.Adam.Lucja.JavaPRO.Controller;

import com.Adam.Lucja.JavaPRO.DTO.Request.AuthRequest;
import com.Adam.Lucja.JavaPRO.DTO.Response.AuthResponse;
import com.Adam.Lucja.JavaPRO.Service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
class HomeController {

    @ModelAttribute("module")
    String module() {
        return "home";
    }

    @Autowired
    AuthService authService;

    @GetMapping({"/","/index"})
    String index(Principal principal) {
//        return principal != null ? "home/homeSignedIn" : "home/homeNotSignedIn";
        System.out.println("narka");
        return "index";
    }

    @PostMapping("/login")
    String loginDetails(@RequestBody MultiValueMap<String, String> formData) {
        AuthRequest authRequest = new AuthRequest(formData.get("username").get(0),formData.get("password").get(0));
        System.out.println("Eluwa "+authRequest.getUsername()+" ukradłem hasło "+authRequest.getPassword());
        AuthResponse response = authService.getAuthToken(authRequest);
        System.out.println(response.getAccessToken());
        return "login";
    }
    @GetMapping("/login")
    String login() {
        System.out.println("siemka");
        return "login";
    }

    @GetMapping("/logout")
    String logout() {
        return "logout";
    }

    @GetMapping("/account")
    String account() {
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
    @GetMapping("/specialController")
    String specialController(){
        return "tak";
    }
}