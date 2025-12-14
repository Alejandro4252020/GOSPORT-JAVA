package com.proyecto.gosports.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;

import com.proyecto.gosports.model.Usuario;
import com.proyecto.gosports.repository.UsuarioRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // Buscar usuario en BD
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        // Rol válido para Spring Security
        String rolSpring = "ROLE_" + usuario.getRol().toUpperCase();

        return new CustomUserDetails(
                usuario,
                Collections.singletonList(new SimpleGrantedAuthority(rolSpring))
        );
    }
}
