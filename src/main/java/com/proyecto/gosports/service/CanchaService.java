package com.proyecto.gosports.service;

import com.proyecto.gosports.model.Cancha;
import com.proyecto.gosports.repository.CanchaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CanchaService {

    private final CanchaRepository repo;

    public CanchaService(CanchaRepository repo) {
        this.repo = repo;
    }

    public List<Cancha> listarTodas() {
        return repo.findAll();
    }

    public Optional<Cancha> obtenerPorId(Long id) {
        return repo.findById(id);
    }

    public Cancha guardar(Cancha cancha) {
        return repo.save(cancha);
    }

    // 👉 Nuevo método para mostrar 3 canchas en el home
    public List<Cancha> listarPrimeras3() {
        return repo.findAll()
                .stream()
                .limit(3)
                .toList();
    }
}
