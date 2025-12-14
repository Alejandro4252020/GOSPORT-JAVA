package com.proyecto.gosports.repository;

import com.proyecto.gosports.model.Carrito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarritoRepository extends JpaRepository<Carrito, Long> {

    // Listar todos los productos del carrito de un usuario
    List<Carrito> findByUsuarioId(Long usuarioId);

    // Buscar un producto específico en el carrito de un usuario
    Optional<Carrito> findByUsuarioIdAndProductoId(Long usuarioId, Long productoId);

    // Eliminar un producto específico del carrito
    @Modifying
    @Transactional
    void deleteByUsuarioIdAndProductoId(Long usuarioId, Long productoId);

    // Limpiar todo el carrito de un usuario
    @Modifying
    @Transactional
    void deleteByUsuarioId(Long usuarioId);
}
