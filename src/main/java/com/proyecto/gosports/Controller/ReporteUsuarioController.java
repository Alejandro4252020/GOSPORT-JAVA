package com.proyecto.gosports.Controller;

import com.proyecto.gosports.model.Usuario;
import com.proyecto.gosports.service.ReporteService;
import com.proyecto.gosports.service.UsuarioService;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ReporteUsuarioController {

    @Autowired
    private ReporteService reporteService;

    @Autowired
    private UsuarioService usuarioService;

    // ===================================
    // REPORTE PDF
    // ===================================
    @GetMapping("/usuarios/reporte/pdf")
    public void generarReportePDF(
            @RequestParam(required = false) Integer id,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String rol,
            HttpServletResponse response) throws Exception {

        List<Usuario> lista = usuarioService.buscarUsuarios(id, username, rol);

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=reporte_usuarios.pdf");

        reporteService.generarReportePdf(lista, response.getOutputStream());
    }

    
    // ===================================
// REPORTE IMPRESIÓN HTML
// ===================================
@GetMapping("/usuarios/reporte/imprimir")
public String imprimirUsuarios(
        @RequestParam(required = false) Integer id,
        @RequestParam(required = false) String username,
        @RequestParam(required = false) String rol,
        org.springframework.ui.Model model) {

    List<Usuario> lista = usuarioService.buscarUsuarios(id, username, rol);

    model.addAttribute("usuarios", lista);

    // "imprimir" es el nombre del template Thymeleaf
    return "imprimir";
}

}

