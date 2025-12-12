package com.proyecto.gosports.service;

import com.proyecto.gosports.model.Cancha;
import com.proyecto.gosports.model.Reserva;
import com.proyecto.gosports.model.Usuario;
import com.proyecto.gosports.repository.CanchaRepository;
import com.proyecto.gosports.repository.ReservaRepository;
import com.proyecto.gosports.repository.UsuarioRepository;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private CanchaRepository canchaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // ------------------------------------------------------
    // 1. CREAR RESERVA
    // ------------------------------------------------------
    public void crearReserva(Long canchaId,
                             Long clienteId,
                             String telefono,
                             LocalDate fecha,
                             LocalTime hora) {

        // VALIDAR CANCHA
        Cancha cancha = canchaRepository.findById(canchaId)
                .orElseThrow(() -> new IllegalArgumentException("La cancha no existe"));

        // VALIDAR CLIENTE
        Usuario cliente = usuarioRepository.findById(clienteId)
                .orElseThrow(() -> new IllegalArgumentException("El cliente no existe"));

        // VALIDAR HORARIO PERMITIDO
        LocalTime apertura = LocalTime.of(6, 0);
        LocalTime cierre = LocalTime.of(22, 0);

        if (hora.isBefore(apertura) || hora.isAfter(cierre)) {
            throw new IllegalArgumentException("Horario no permitido (6:00 AM - 10:00 PM)");
        }

        // VALIDAR QUE NO EXISTA OTRA RESERVA
        boolean ocupada = reservaRepository
                .findByCancha_IdAndFechaAndHora(canchaId, fecha, hora)
                .isPresent();

        if (ocupada) {
            throw new IllegalArgumentException("La cancha ya está reservada en ese horario");
        }

        // CREAR RESERVA
        Reserva r = new Reserva();
        r.setCancha(cancha);
        r.setCliente(cliente);
        r.setTelefono(telefono);
        r.setFecha(fecha);
        r.setHora(hora);

        reservaRepository.save(r);
    }

    // ------------------------------------------------------
    // 2. LISTAR TODAS LAS RESERVAS
    // ------------------------------------------------------
    public List<Reserva> listar() {
        return reservaRepository.findAll();
    }

    // ------------------------------------------------------
    // 3. FILTRAR POR RANGO DE FECHAS
    // ------------------------------------------------------
    public List<Reserva> filtrarPorFecha(LocalDate inicio, LocalDate fin) {
        return reservaRepository.findByFechaBetween(inicio, fin);
    }

    // ------------------------------------------------------
    // 4. GENERAR PDF
    // ------------------------------------------------------
    public void generarPdf(List<Reserva> reservas, HttpServletResponse response) {
        try {
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=reporte_reservas.pdf");

            Document document = new Document();
            PdfWriter.getInstance(document, response.getOutputStream());

            document.open();
            document.add(new Paragraph("REPORTE DE RESERVAS"));
            document.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(4);
            table.addCell("ID");
            table.addCell("Cliente");
            table.addCell("Fecha");
            table.addCell("Cancha");

            for (Reserva r : reservas) {
                table.addCell(r.getId().toString());
                table.addCell(r.getCliente().getUserName());
                table.addCell(r.getFecha().toString());
                table.addCell(r.getCancha().getNombre());
            }

            document.add(table);
            document.close();

        } catch (DocumentException | IOException e) {
            throw new RuntimeException("Error al generar el PDF", e);
        }
    }

    // ------------------------------------------------------
    // 5. HISTORIAL DE RESERVAS POR CLIENTE
    // ------------------------------------------------------
    public List<Reserva> historialPorCliente(Long clienteId) {
        return reservaRepository.findByCliente_Id(clienteId);
    }

    // ------------------------------------------------------
    // 6. OBTENER RESERVA POR ID
    // ------------------------------------------------------
    public Reserva getReserva(Long id) {
        return reservaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));
    }

    // ------------------------------------------------------
    // 7. ACTUALIZAR RESERVA
    // ------------------------------------------------------
    public Reserva actualizarReserva(Reserva reserva) {
        return reservaRepository.save(reserva);
    }
}
