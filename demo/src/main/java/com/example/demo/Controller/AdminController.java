package com.example.demo.Controller;


import com.example.demo.Entités.Users;
import com.example.demo.UserService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
/**
 * Cette annotation spécifie que la méthode ou la classe est accessible uniquement aux utilisateurs ayant le rôle "ROLE_ADMIN".
 * Si l'utilisateur n'a pas ce rôle, une exception d'accès refusé (AccessDeniedException) sera lancée.
 */
@Secured("ROLE_ADMIN")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    // Affiche la liste des utilisateurs
    @GetMapping
    public String adminPage(Model model) {
       // model.addAttribute : Cette méthode permet d'ajouter des attributs au modèle, qui sont ensuite accessibles dans la vue rendue.
        model.addAttribute("users", userService.getAllUsers());
        return "admin"; // Vue "admin.html"
    }

    // Formulaire pour ajouter un nouvel utilisateur
    @GetMapping("/add")
    public String addUserForm(Model model) {
        model.addAttribute("user", new Users());
        return "add-user"; // Vue "add-user.html"
    }

    // Soumission du formulaire pour ajouter un utilisateur
    @PostMapping("/add")
    public String addUser(@ModelAttribute Users user) {
        userService.saveUser(user);
        return "redirect:/admin"; // Redirige vers la page admin
    }

    // Formulaire pour modifier un utilisateur existant
    @GetMapping("/edit/{id}")
    public String editUserForm(@PathVariable Long id, Model model) {
        model.addAttribute("user", userService.getUserById(id));
        return "edit-user"; // Vue "edit-user.html"
    }

    // Soumission du formulaire pour modifier un utilisateur
    @PostMapping("/edit/{id}")
    public String editUser(@PathVariable Long id, @ModelAttribute Users user) {
        userService.updateUser(id, user);
        return "redirect:/admin";
    }

    // Suppression d'un utilisateur
    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }
}



