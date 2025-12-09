package com.proyecto.gosports.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.proyecto.gosports.model.Reserva;
import java.time.LocalDate;
import java.util.List;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    List<Reserva> findByFechaBetween(LocalDate inicio, LocalDate fin);
}
