package com.proyecto.gosports.service;

import com.proyecto.gosports.model.Usuario;
import com.proyecto.gosports.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // ---------------------------------------------------------
    // MÉTODOS PARA QUE EL CONTROLADOR NO USE Optional
    // ---------------------------------------------------------

    public Usuario getByEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con email: " + email));
    }

    public Usuario getByUserName(String username) {
        return usuarioRepository.findByUserName(username)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con username: " + username));
    }

    // ---------------------------------------------------------
    // MÉTODOS ORIGINALES
    // ---------------------------------------------------------

    public Optional<Usuario> findByUserName(String username) {
        return usuarioRepository.findByUserName(username);
    }

    public Optional<Usuario> findByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public List<Usuario> buscarUsuarios(Integer id, String username, String rol) {

        if (id == null && (username == null || username.isEmpty()) && 
            (rol == null || rol.isEmpty())) {
            return usuarioRepository.findAll();
        }

        if (id != null) {
            return usuarioRepository.findById(id.longValue())
                    .map(List::of)
                    .orElse(List.of());
        }

        if (username != null && !username.isEmpty()) {
            return usuarioRepository.findByUserNameContainingIgnoreCase(username);
        }

        if (rol != null && !rol.isEmpty()) {
            return usuarioRepository.findByRol(rol);
        }

        return usuarioRepository.findAll();
    }
}
