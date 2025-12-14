package com.proyecto.gosports.repository;

import com.proyecto.gosports.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository
        extends JpaRepository<Usuario, Long>, JpaSpecificationExecutor<Usuario> {

    // 🔹 Usuario por username
    Optional<Usuario> findByUsername(String username);

    // 🔹 Usuario por email
    Optional<Usuario> findByEmail(String email);

    // 🔹 Búsqueda por username (insensible a mayúsculas)
    List<Usuario> findByUsernameContainingIgnoreCase(String username);

    // 🔹 Buscar por rol (ROLE_USER / ROLE_ADMIN)
    List<Usuario> findByRol(String rol);
}

