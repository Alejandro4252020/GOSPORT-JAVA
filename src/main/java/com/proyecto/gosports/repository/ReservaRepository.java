package com.proyecto.gosports.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.proyecto.gosports.model.Reserva;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    /**
     * Buscar reservas dentro de un rango de fechas.
     */
    List<Reserva> findByFechaBetween(LocalDate inicio, LocalDate fin);

    /**
     * Validar si existe una reserva en la misma cancha / fecha / hora.
     * Esto se usa para impedir reservas duplicadas.
     */
    Optional<Reserva> findByCancha_IdAndFechaAndHora(
        Long canchaId,
        LocalDate fecha,
        LocalTime hora
    );

    /**
     * Buscar todas las reservas de un cliente (usuario) por su ID.
     */
    List<Reserva> findByCliente_Id(Long clienteId);
}
