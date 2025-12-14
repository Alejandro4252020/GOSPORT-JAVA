package com.proyecto.gosports.Controller;

import com.proyecto.gosports.model.Carrito;
import com.proyecto.gosports.model.Producto;
import com.proyecto.gosports.model.Usuario;
import com.proyecto.gosports.repository.CarritoRepository;
import com.proyecto.gosports.repository.ProductoRepository;
import com.proyecto.gosports.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/carrito")
public class CarritoController {

    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // =====================
    // OBTENER USUARIO LOGUEADO (POR USERNAME)
    // =====================
    private Optional<Usuario> getUsuarioLogueado() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal().equals("anonymousUser")) {
            return Optional.empty();
        }

        String username = auth.getName(); // usar username, no email
        return usuarioRepository.findByUsername(username);
    }

    // ---------------------
    // VER CARRITO
    // ---------------------
    @GetMapping
    public String verCarrito(Model model) {

        Optional<Usuario> usuarioOpt = getUsuarioLogueado();
        if (usuarioOpt.isEmpty()) {
            return "redirect:/login";
        }

        Long usuarioId = usuarioOpt.get().getId();
        List<Carrito> carrito = carritoRepository.findByUsuarioId(usuarioId);

        Map<Long, Producto> productos = new HashMap<>();
        for (Carrito c : carrito) {
            productoRepository.findById(c.getProductoId())
                    .ifPresent(p -> productos.put(c.getProductoId(), p));
        }

        double total = carrito.stream()
                .mapToDouble(c -> {
                    Producto p = productos.get(c.getProductoId());
                    return p != null ? p.getPrecio() * c.getCantidad() : 0;
                })
                .sum();

        model.addAttribute("carrito", carrito);
        model.addAttribute("productos", productos);
        model.addAttribute("total", total);

        return "carrito";
    }

    // ---------------------
    // AGREGAR PRODUCTO
    // ---------------------
    @PostMapping("/agregar")
    @Transactional
    public String agregarAlCarrito(@RequestParam Long productoId) {

        Optional<Usuario> usuarioOpt = getUsuarioLogueado();
        if (usuarioOpt.isEmpty()) {
            return "redirect:/login";
        }

        Long usuarioId = usuarioOpt.get().getId();

        Carrito carrito = carritoRepository
                .findByUsuarioIdAndProductoId(usuarioId, productoId)
                .orElse(new Carrito(usuarioId, productoId, 0));

        carrito.setCantidad(carrito.getCantidad() + 1);
        carritoRepository.save(carrito);

        return "redirect:/carrito";
    }

    // ---------------------
    // REDUCIR CANTIDAD
    // ---------------------
    @PostMapping("/reducir")
    @Transactional
    public String reducirCantidad(@RequestParam Long productoId) {

        Optional<Usuario> usuarioOpt = getUsuarioLogueado();
        if (usuarioOpt.isEmpty()) {
            return "redirect:/login";
        }

        Long usuarioId = usuarioOpt.get().getId();

        carritoRepository.findByUsuarioIdAndProductoId(usuarioId, productoId)
                .ifPresent(c -> {
                    if (c.getCantidad() > 1) {
                        c.setCantidad(c.getCantidad() - 1);
                        carritoRepository.save(c);
                    } else {
                        carritoRepository.delete(c);
                    }
                });

        return "redirect:/carrito";
    }

    // ---------------------
    // ELIMINAR PRODUCTO
    // ---------------------
    @PostMapping("/eliminar")
    @Transactional
    public String eliminarProducto(@RequestParam Long productoId) {

        Optional<Usuario> usuarioOpt = getUsuarioLogueado();
        if (usuarioOpt.isEmpty()) {
            return "redirect:/login";
        }

        carritoRepository.deleteByUsuarioIdAndProductoId(
                usuarioOpt.get().getId(),
                productoId
        );

        return "redirect:/carrito";
    }

    // ---------------------
    // VACIAR CARRITO
    // ---------------------
    @PostMapping("/vaciar")
    @Transactional
    public String vaciarCarrito() {

        Optional<Usuario> usuarioOpt = getUsuarioLogueado();
        if (usuarioOpt.isEmpty()) {
            return "redirect:/login";
        }

        carritoRepository.deleteByUsuarioId(usuarioOpt.get().getId());

        return "redirect:/carrito";
    }
}
