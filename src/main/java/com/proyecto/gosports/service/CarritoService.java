package com.proyecto.gosports.service;


import com.proyecto.gosports.model.Carrito;
import com.proyecto.gosports.repository.CarritoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarritoService {

    @Autowired
    private CarritoRepository repo;

    public void agregarProducto(Integer usuarioId, Integer productoId) {
        var existente = repo.findByUsuarioIdAndProductoId(usuarioId, productoId);

        if (existente.isPresent()) {
            Carrito item = existente.get();
            item.setCantidad(item.getCantidad() + 1);
            repo.save(item);
        } else {
            Carrito nuevo = new Carrito(usuarioId, productoId, 1);
            repo.save(nuevo);
        }
    }

    public List<Carrito> listar(Integer usuarioId) {
        return repo.findByUsuarioId(usuarioId);
    }
}
