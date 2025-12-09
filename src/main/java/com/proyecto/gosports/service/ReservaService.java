package com.proyecto.gosports.service;

import com.proyecto.gosports.model.Reserva;
import com.proyecto.gosports.repository.ReservaRepository;

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
import java.util.List;

@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    // LISTAR TODAS LAS RESERVAS
    public List<Reserva> listar() {
        return reservaRepository.findAll();
    }

    // FILTRAR POR FECHA
    public List<Reserva> filtrarPorFecha(LocalDate inicio, LocalDate fin) {
        return reservaRepository.findByFechaBetween(inicio, fin);
    }

    // GENERAR PDF
    public void generarPdf(List<Reserva> reservas, HttpServletResponse response) {
        try {
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=reporte_reservas.pdf");

            Document document = new Document();
            PdfWriter.getInstance(document, response.getOutputStream());

            document.open();

            // TÍTULO
            document.add(new Paragraph("REPORTE DE RESERVAS"));
            document.add(new Paragraph(" "));

            // TABLA (4 columnas: ID, cliente, fecha, cancha)
            PdfPTable table = new PdfPTable(4);

            table.addCell("ID");
            table.addCell("Cliente");
            table.addCell("Fecha");
            table.addCell("Cancha");

            for (Reserva r : reservas) {
                table.addCell(r.getId() != null ? r.getId().toString() : "N/A");
                table.addCell(r.getCliente() != null ? r.getCliente() : "N/A");
                table.addCell(r.getFecha() != null ? r.getFecha().toString() : "N/A");
                table.addCell(r.getCancha() != null ? r.getCancha() : "N/A");
            }

            document.add(table);
            document.close();

        } catch (DocumentException | IOException e) {
            throw new RuntimeException("Error al generar el PDF", e);
        }
    }
}
