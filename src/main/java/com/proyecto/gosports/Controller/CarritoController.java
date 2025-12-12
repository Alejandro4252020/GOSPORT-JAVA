package com.proyecto.gosports.Controller;

import com.proyecto.gosports.service.CarritoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carrito")
public class CarritoController {

    @Autowired
    private CarritoService service;

    @PostMapping("/agregar")
    public String agregar(
            @RequestParam Integer productoId,
            @RequestParam Integer usuarioId) {

        service.agregarProducto(usuarioId, productoId);
        return "OK";
    }

    @GetMapping("/listar")
    public Object listar(@RequestParam Integer usuarioId) {
        return service.listar(usuarioId);
    }
}
