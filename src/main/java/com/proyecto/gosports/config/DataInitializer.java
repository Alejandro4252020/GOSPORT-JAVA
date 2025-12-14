package com.proyecto.gosports.config;

import com.proyecto.gosports.model.Usuario;
import com.proyecto.gosports.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initDatabase(UsuarioRepository repo) {
        return args -> {

            // 🔹 Usar username (NO username)
            if (repo.findByUsername("admin").isEmpty()) {

                Usuario admin = new Usuario();
                admin.setUsername("admin");
                admin.setPassword(new BCryptPasswordEncoder().encode("Esteban1234"));

                // 🔹 Usar formato correcto de Spring Security
                admin.setRol("ROLE_ADMIN");

                repo.save(admin);
                System.out.println("✅ Usuario admin creado con éxito");

            } else {
                System.out.println("ℹ️ Usuario admin ya existe");
            }
        };
    }
}
