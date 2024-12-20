package com.example.demo;


import com.example.demo.Entités.Role;
import com.example.demo.Entités.Users;
import com.example.demo.Repositories.RoleRepository;
import com.example.demo.Repositories.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DataLoader {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
 // Méthode exécutée après la construction de l'objet pour initialiser la base de données
    @PostConstruct
    public void init() {
        Role adminRole = new Role();
        adminRole.setName("ROLE_ADMIN");
        roleRepository.save(adminRole);

        Role userRole = new Role();
        userRole.setName("ROLE_USER");
        roleRepository.save(userRole);
        // Création et sauvegarde de l'utilisateur "admin" avec le rôle "ROLE_ADMIN"
        Users admin = new Users();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setEmail("admin@example.com");
        admin.setRoles(Set.of(adminRole));
        userRepository.save(admin);

        Users user = new Users();
        user.setUsername("user");
        user.setPassword(passwordEncoder.encode("user123"));
        user.setEmail("user@example.com");
        user.setRoles(Set.of(userRole));
        userRepository.save(user);
    }
}