package com.example.demo.Controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class User_Controller {
    @GetMapping("/")
    public String index() {
        return "redirect:/home";
    }


    @GetMapping("/home")
    public String home() {
        // Récupère l'objet Authentication qui contient les informations sur l'utilisateur actuellement authentifié
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        // Affiche le nom de l'utilisateur connecté
        System.out.println("Utilisateur connecté : " + auth.getName());
        System.out.println("Rôles : " + auth.getAuthorities());
        // Vérifie si l'utilisateur a le rôle ADMIN
        if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            return "redirect:/admin"; // Redirige vers la page d'administration
        }

        // Par défaut, redirige vers la page utilisateur
        return "home"; // Retourne la vue "home.html"
    }
    }
