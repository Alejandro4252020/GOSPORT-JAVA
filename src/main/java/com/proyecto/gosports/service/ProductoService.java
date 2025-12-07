package com.proyecto.gosports.service;

import com.proyecto.gosports.model.Producto;
import com.proyecto.gosports.model.Usuario;
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

    public List<Producto> listar() {
        return repo.findAll();
    }

    public Producto insertar(Producto p) {
        return repo.save(p);
    }

    public Producto actualizar(Long id, Producto p) {
        Producto existente = obtenerPorId(id);
        existente.setNombre(p.getNombre());
        existente.setPrecio(p.getPrecio());
        return repo.save(existente);
    }

    public void eliminar(Long id) {
        repo.deleteById(id);
    }

    public Producto obtenerPorId(Long id) {
        return repo.findById(id).orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));
    }
	public interface ReporteService {

    void generarReportePdf(List<Usuario> lista, OutputStream out) throws Exception;

    void generarReporteExcel(List<Usuario> lista, OutputStream out) throws Exception;

    void generarReportePlano(List<Usuario> lista, OutputStream out) throws Exception;
}

}
