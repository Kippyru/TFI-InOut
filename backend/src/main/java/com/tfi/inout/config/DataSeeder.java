package com.tfi.inout.config;

import com.tfi.inout.model.Role;
import com.tfi.inout.model.User;
import com.tfi.inout.repository.RoleRepository;
import com.tfi.inout.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DataSeeder implements CommandLineRunner {
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${app.admin.username:admin}")
    private String adminUsername;
    @Value("${app.admin.password:1234}")
    private String adminPassword;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        //automaticamente crea los rolesh
        Role adminRole = roleRepository.findByName("ADMIN").orElseGet(() -> {
            Role role = new Role();
            role.setName("ADMIN");
            return roleRepository.save(role);
        });

        roleRepository.findByName("EMPLOYEE").orElseGet(() -> {
            Role role = new Role();
            role.setName("EMPLOYEE");
            return roleRepository.save(role);
        });

        //y aca crea el admin
        userRepository.findByUsername(adminUsername).orElseGet(() -> {
            User admin = new User();
            admin.setUsername(adminUsername);
            admin.setPassword(passwordEncoder.encode(adminPassword));
            admin.setRole(adminRole);
            return userRepository.save(admin);
        });
    }
}
