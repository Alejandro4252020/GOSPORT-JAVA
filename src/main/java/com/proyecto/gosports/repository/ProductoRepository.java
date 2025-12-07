package com.proyecto.gosports.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.proyecto.gosports.model.Producto;


public interface ProductoRepository extends JpaRepository<Producto, Long> {
} 