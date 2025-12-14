package com.proyecto.gosports.service;

import com.proyecto.gosports.model.Producto;
import com.proyecto.gosports.repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.util.List;

@Service
public class ProductoService {

    private final ProductoRepository repo;

    public ProductoService(ProductoRepository repo) {
        this.repo = repo;
    }

    // LISTAR TODOS
    public List<Producto> listar() {
        return repo.findAll();
    }

    // INSERTAR NUEVO
    public Producto insertar(Producto p) {
        return repo.save(p);
    }

    // ACTUALIZAR EXISTENTE
    public Producto actualizar(Long id, Producto p) {
        Producto existente = obtenerPorId(id);
        existente.setNombre(p.getNombre());
        existente.setPrecio(p.getPrecio());
        return repo.save(existente);
    }

    // ELIMINAR POR ID
    public void eliminar(Long id) {
        repo.deleteById(id);
    }

    // OBTENER POR ID
    public Producto obtenerPorId(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));
    }

    // ===== Interface de reportes para productos =====
    public interface ReporteService {

        void generarReportePdf(List<Producto> lista, OutputStream out) throws Exception;

        void generarReporteExcel(List<Producto> lista, OutputStream out) throws Exception;

        void generarReportePlano(List<Producto> lista, OutputStream out) throws Exception;
    }
    // LISTAR PRIMEROS 3 PRODUCTOS (para el home)
    public List<Producto> listarPrimeros3() {
        return repo.findAll().stream().limit(3).toList();
    }

}
