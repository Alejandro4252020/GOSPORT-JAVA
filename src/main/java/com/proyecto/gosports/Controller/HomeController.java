package com.proyecto.gosports.Controller;

import com.proyecto.gosports.model.Producto;
import com.proyecto.gosports.service.CanchaService;
import com.proyecto.gosports.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/home")
public class HomeController {

    @Autowired
    private CanchaService canchaService;

    @Autowired
    private ProductoService productoService;

    @GetMapping
    public String home(Model model) {

        // Cargar las primeras 3 canchas para el home
        model.addAttribute("canchas", canchaService.listarPrimeras3());

        // Cargar los primeros 3 productos para el home
        List<Producto> productos = productoService.listarPrimeros3();
        model.addAttribute("productos", productos);

        return "home";
    }
}
