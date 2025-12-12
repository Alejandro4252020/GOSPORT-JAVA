package com.proyecto.gosports.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;

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
                    "/registrar",
                    "/registrar/**",
                    "/h2-console/**"
                )
            )

            // =======================
            // H2 FRAME OPTIONS
            // =======================
            .headers(headers -> headers
                .frameOptions(frame -> frame.disable())
            )

            // =======================
            // AUTH RULES
            // =======================
            .authorizeHttpRequests(auth -> auth
                // PÚBLICO
                .requestMatchers(
                    "/", 
                    "/login", 
                    "/paginaprincipal",
                    "/registrar",
                    "/registrar/**",
                    "/forgot-password",
                    "/reset-password",
                    "/css/**",
                    "/js/**",
                    "/img/**",
                    "/h2-console/**"
                ).permitAll()

                // ADMIN
                .requestMatchers("/admin/**").hasRole("ADMIN")

                // PROTEGIDAS
                .requestMatchers("/reservar-cancha").authenticated()
                .requestMatchers("/reservas/**").authenticated()

                // CUALQUIER OTRA REQUIERE LOGIN
                .anyRequest().authenticated()
            )

            // =======================
            // LOGIN FORM
            // =======================
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/home", true)
                .failureUrl("/login?error=true")
                .permitAll()
            )

            // =======================
            // LOGOUT
            // =======================
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/paginaprincipal?logout")
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
