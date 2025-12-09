package com.proyecto.gosports.service;

import com.proyecto.gosports.model.Usuario;
import com.proyecto.gosports.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.proyecto.gosports.service.UsuarioService;


import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Usuario> buscarUsuarios(Integer id, String username, String rol) {

        // Si no hay filtros → todo
        if (id == null && (username == null || username.isEmpty()) && (rol == null || rol.isEmpty())) {
            return usuarioRepository.findAll();
        }

        // Filtrar por ID
        if (id != null) {
            return usuarioRepository.findById(id.longValue())
                    .map(List::of)
                    .orElse(List.of());
        }

        // Filtrar por username
        if (username != null && !username.isEmpty()) {
            return usuarioRepository.findByUserNameContainingIgnoreCase(username);
        }

        // Filtrar por rol
        if (rol != null && !rol.isEmpty()) {
            return usuarioRepository.findByRol(rol);
        }

        return usuarioRepository.findAll();
    }
}
