package com.cenaflixpodcast.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {
    
    @GetMapping("/")
    public String home() {
        return "redirect:/login";
    }
    
    @GetMapping("/login")
    public String showLoginPage(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout,
            @RequestParam(value = "expired", required = false) String expired,
            Model model) {
        
        if (error != null) {
            model.addAttribute("error", "Usuário ou senha inválidos!");
        }
        if (logout != null) {
            model.addAttribute("message", "Você saiu do sistema com sucesso!");
        }
        if (expired != null) {
            model.addAttribute("error", "Sessão expirada. Faça login novamente.");
        }
        
        return "login";
    }
}
