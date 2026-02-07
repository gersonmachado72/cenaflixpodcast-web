package com.cenaflixpodcast.controller.web;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MenuController {

    @GetMapping("/menu")
    @PreAuthorize("isAuthenticated()")
    public String showMenu(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            model.addAttribute("nomeUsuario", auth.getName());
            model.addAttribute("tipoUsuario", auth.getAuthorities());
        }
        return "menu";
    }
}
