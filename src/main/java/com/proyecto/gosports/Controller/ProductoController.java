package com.proyecto.gosports.Controller;


import com.proyecto.gosports.model.Producto;
import com.proyecto.gosports.service.ProductoService;
import com.proyecto.gosports.service.ReporteService;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/productos")
public class ProductoController {

    private final ProductoService servicio;

    public ProductoController(ProductoService servicio, ReporteService reporteService) {
        this.servicio = servicio;
    }

    // ===== CRUD =====
    @GetMapping
    public String listar(Model modelo) {
        modelo.addAttribute("productos", servicio.listar());
        return "listar";
    }

    @GetMapping("/nuevo")
    public String nuevoFormulario(Model modelo) {
        modelo.addAttribute("producto", new Producto());
        return "formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Producto producto) {
        servicio.insertar(producto);
        return "redirect:/productos";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model modelo) {
        Producto producto = servicio.obtenerPorId(id);
        modelo.addAttribute("producto", producto);
        return "formulario";
    }

    @PostMapping("/actualizar/{id}")
    public String actualizar(@PathVariable Long id, @ModelAttribute Producto producto) {
        servicio.actualizar(id, producto);
        return "redirect:/productos";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        servicio.eliminar(id);
        return "redirect:/productos";
    }
    @GetMapping("/detalle/{id}")
    public String verDetalleProducto(@PathVariable Long id, Model model) {
        Producto producto = servicio.obtenerPorId(id);
        model.addAttribute("producto", producto);
        return "tarjetaproducto";
    }


    
}
