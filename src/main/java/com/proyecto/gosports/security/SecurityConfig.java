package com.proyecto.gosports.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailsService;

    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            // =======================
            // CSRF
            // =======================
            .csrf(csrf -> csrf
                .ignoringRequestMatchers(
                    "/registrar/**",
                    "/h2-console/**"
                )
            )

            // =======================
            // H2
            // =======================
            .headers(headers -> headers
                .frameOptions(frame -> frame.disable())
            )

            // =======================
            // AUTORIZACIÓN
            // =======================
            .authorizeHttpRequests(auth -> auth

                // PÚBLICO
                .requestMatchers(
                    "/login",
                    "/paginaprincipal",
                    "/registrar/**",
                    "/forgot-password",
                    "/reset-password",
                    "/css/**",
                    "/js/**",
                    "/img/**",
                    "/uploads/**",
                    "/h2-console/**"
                ).permitAll()


                // PERFIL
                .requestMatchers("/mi-perfil/**").authenticated()

                // RESERVAS
                .requestMatchers(
                    "/reservar-cancha",
                    "/reservas/**"
                ).authenticated()

               // ADMIN
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )

            // =======================
            // LOGIN
            // =======================
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/home") // 🔥 CLAVE
                .failureUrl("/login?error=true")
                .permitAll()
            )


            // =======================
            // LOGOUT
            // =======================
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .permitAll()
            )

            // =======================
            // USER DETAILS
            // =======================
            .userDetailsService(userDetailsService);

        return http.build();
    }

    // =======================
    // PASSWORD ENCODER
    // =======================
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // =======================
    // AUTH MANAGER
    // =======================
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
