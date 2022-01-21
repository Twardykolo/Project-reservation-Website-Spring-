package com.Adam.Lucja.JavaPRO.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import java.security.Principal;

@Controller
class HomeController {

    @ModelAttribute("module")
    String module() {
        return "home";
    }

    @GetMapping("/")
    String index(Principal principal) {
//        return principal != null ? "home/homeSignedIn" : "home/homeNotSignedIn";
        return "index";
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
    String account() {
        return "account";
    }

    @GetMapping("/register")
    String register() {
        return "register";

    @GetMapping("/loginZepsuty")
    String loginFailed(Model model){
        model.addAttribute("loginError", true);
        return "login";
    }
}