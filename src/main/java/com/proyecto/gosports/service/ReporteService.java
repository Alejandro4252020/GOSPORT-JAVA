package com.proyecto.gosports.service;

import java.io.IOException;
import java.util.List;

import com.proyecto.gosports.model.Usuario;
import jakarta.servlet.ServletOutputStream;

import org.springframework.stereotype.Service;

@Service
public class ReporteService {

    public void generarReportePlano(List<Usuario> lista, ServletOutputStream outputStream) throws IOException {
        for (Usuario u : lista) {
            String linea = u.getId() + " | " + u.getUserName() + " | " + u.getRol() + "\n";
            outputStream.write(linea.getBytes());
        }
        outputStream.flush();
    }

    public void generarReporteExcel(List<Usuario> lista, ServletOutputStream outputStream) throws IOException {
        // Ejemplo simple CSV para Excel
        outputStream.write("ID,Username,Rol\n".getBytes());
        for (Usuario u : lista) {
            String linea = u.getId() + "," + u.getUserName() + "," + u.getRol() + "\n";
            outputStream.write(linea.getBytes());
        }
        outputStream.flush();
    }

    public void generarReportePdf(List<Usuario> lista, ServletOutputStream outputStream) throws IOException {
        // PDF básico como texto plano (para ejemplo)
        outputStream.write("REPORTE DE USUARIOS\n\n".getBytes());
        for (Usuario u : lista) {
            String linea = "ID: " + u.getId() + ", Username: " + u.getUserName() + ", Rol: " + u.getRol() + "\n";
            outputStream.write(linea.getBytes());
        }
        outputStream.flush();
    }
}
