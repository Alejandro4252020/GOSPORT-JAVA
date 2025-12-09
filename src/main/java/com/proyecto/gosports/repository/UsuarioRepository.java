package com.proyecto.gosports.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import com.proyecto.gosports.model.Usuario;

public interface UsuarioRepository 
        extends JpaRepository<Usuario, Long>, JpaSpecificationExecutor<Usuario> {

    Optional<Usuario> findByUserName(String userName);

    List<Usuario> findByUserNameContainingIgnoreCase(String userName);

    List<Usuario> findByRol(String rol);
}
