package com.proyecto.gosports.Controller;

import com.proyecto.gosports.model.Cancha;
import com.proyecto.gosports.service.CanchaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/canchas")
public class CanchaController {

    @Autowired
    private CanchaService canchaService;

    // 👉 LISTA DE CANCHAS
    @GetMapping
    public String mostrarCanchas(Model model) {
        model.addAttribute("canchas", canchaService.listarTodas());
        return "canchas"; // tu catálogo
    }

    // 👉 DETALLE DE CANCHA
    @GetMapping("/{id}")
    public String detalleCancha(@PathVariable Long id, Model model) {
        Cancha cancha = canchaService.obtenerPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Cancha no encontrada: " + id));

        model.addAttribute("cancha", cancha);
        return "detalle-cancha";  // vista de detalle
    }

    // 👉 RESERVAR
    @GetMapping("/reservar/{id}")
    public String reservarCancha(@PathVariable Long id, Model model) {
        Cancha cancha = canchaService.obtenerPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Cancha no encontrada: " + id));

        model.addAttribute("cancha", cancha);
        return "reservar-cancha";
    }
}
