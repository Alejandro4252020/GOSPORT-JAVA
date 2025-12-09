package com.proyecto.gosports.Controller;

import com.proyecto.gosports.model.Reserva;
import com.proyecto.gosports.service.ReservaService;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/reportes/reservas")
public class ReporteReservaController {

    @Autowired
    private ReservaService reservaService;

    // ============================
    // MOSTRAR REPORTE EN HTML
    // ============================
    @GetMapping
    public String mostrarReporte(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,

            Model model) {

        List<Reserva> reservas;

        if (fechaInicio == null || fechaFin == null) {
            reservas = reservaService.listar();  // <── CORREGIDO
        } else {
            reservas = reservaService.filtrarPorFecha(fechaInicio, fechaFin); // <── CORREGIDO
        }

        model.addAttribute("reservas", reservas);
        model.addAttribute("fechaInicio", fechaInicio);
        model.addAttribute("fechaFin", fechaFin);

        return "reporte_reservas";
    }

    // ============================
    // GENERAR PDF
    // ============================
    @GetMapping("/pdf")
    public void generarPdf(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,

            HttpServletResponse response) throws IOException {

        List<Reserva> reservas;

        if (fechaInicio == null || fechaFin == null) {
            reservas = reservaService.listar();
        } else {
            reservas = reservaService.filtrarPorFecha(fechaInicio, fechaFin);
        }

        reservaService.generarPdf(reservas, response);
    }
}
