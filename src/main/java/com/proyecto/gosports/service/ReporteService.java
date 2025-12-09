package com.proyecto.gosports.service;

import java.io.IOException;

import java.util.List;

import com.proyecto.gosports.model.Usuario;
import jakarta.servlet.ServletOutputStream;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

@Service
public class ReporteService {

    // ===================================
    // REPORTE PLANO (TXT)
    // ===================================
    public void generarReportePlano(List<Usuario> lista, ServletOutputStream outputStream) throws IOException {
        for (Usuario u : lista) {
            String linea = u.getId() + " | " + u.getUserName() + " | " + u.getRol() + "\n";
            outputStream.write(linea.getBytes());
        }
        outputStream.flush();
    }

    // ===================================
    // REPORTE EXCEL REAL (.xlsx)
    // ===================================
    public void generarReporteExcel(List<Usuario> lista, ServletOutputStream outputStream) throws IOException {
        try {
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("Usuarios");

            // Encabezados
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("ID");
            header.createCell(1).setCellValue("Username");
            header.createCell(2).setCellValue("Rol");

            // Datos
            int rowIndex = 1;
            for (Usuario u : lista) {
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(u.getId());
                row.createCell(1).setCellValue(u.getUserName());
                row.createCell(2).setCellValue(u.getRol());
            }

            workbook.write(outputStream);
            workbook.close();
        } catch (Exception e) {
            throw new IOException("Error al generar Excel", e);
        }
    }

    // ===================================
    // REPORTE PDF
    // ===================================
    public void generarReportePdf(List<Usuario> lista, ServletOutputStream outputStream) throws IOException {
        try {
            com.lowagie.text.Document document = new com.lowagie.text.Document();
            com.lowagie.text.pdf.PdfWriter.getInstance(document, outputStream);

            document.open();

            com.lowagie.text.Font tituloFont = new com.lowagie.text.Font(
                    com.lowagie.text.Font.HELVETICA, 18, com.lowagie.text.Font.BOLD
            );
            com.lowagie.text.Font textoFont = new com.lowagie.text.Font(
                    com.lowagie.text.Font.HELVETICA, 12
            );

            document.add(new com.lowagie.text.Paragraph("REPORTE DE USUARIOS\n\n", tituloFont));

            for (Usuario u : lista) {
                String linea = "ID: " + u.getId()
                        + "   | Usuario: " + u.getUserName()
                        + "   | Rol: " + u.getRol();

                document.add(new com.lowagie.text.Paragraph(linea, textoFont));
            }

            document.close();
        } catch (Exception e) {
            throw new IOException("Error al generar el PDF", e);
        }
    }

}
