package com.proyecto.gosports.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Productos")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private Double precio;

    // Getters y Setters
}
