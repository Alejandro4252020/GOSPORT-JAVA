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
    private String imagen; // 👈 NUEVO
    @Column(length = 500)
    private String descripcion;

    // getters y setters
    public String getDescripcion() {
    return descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }



}
