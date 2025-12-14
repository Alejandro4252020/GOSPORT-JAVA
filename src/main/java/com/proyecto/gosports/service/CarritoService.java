package com.proyecto.gosports.service;

import com.proyecto.gosports.model.Carrito;
import com.proyecto.gosports.model.Usuario;
import com.proyecto.gosports.repository.CarritoRepository;
import com.proyecto.gosports.repository.UsuarioRepository;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CarritoService {

    @Autowired
    private CarritoRepository repo;

    @Autowired
    private UsuarioRepository usuarioRepository;

    /* =========================
       MÉTODO CENTRAL
       ========================= */
    private Usuario obtenerUsuarioLogueado() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()
                || auth.getPrincipal().equals("anonymousUser")) {
            return null;
        }

        String email = auth.getName();
        return usuarioRepository.findByEmail(email).orElse(null);
    }

    /* =========================
       AGREGAR PRODUCTO
       ========================= */
    @Transactional
    public void agregarProducto(Long productoId, int cantidad, HttpSession session) {

        Usuario usuario = obtenerUsuarioLogueado();
        if (usuario == null) return;

        Long usuarioId = usuario.getId();
        Long prodId = productoId;

        Carrito item = repo.findByUsuarioIdAndProductoId(usuarioId, prodId)
                .orElse(null);

        if (item != null) {
            item.setCantidad(item.getCantidad() + cantidad);
        } else {
            item = new Carrito(usuarioId, prodId, cantidad);
        }

        repo.save(item);
    }

    /* =========================
       REDUCIR CANTIDAD
       ========================= */
    @Transactional
    public void reducirCantidad(Long productoId, HttpSession session) {

        Usuario usuario = obtenerUsuarioLogueado();
        if (usuario == null) return;

        Long usuarioId = usuario.getId();
        Long prodId = productoId;

        Carrito item = repo.findByUsuarioIdAndProductoId(usuarioId, prodId)
                .orElse(null);

        if (item != null) {
            if (item.getCantidad() > 1) {
                item.setCantidad(item.getCantidad() - 1);
                repo.save(item);
            } else {
                repo.delete(item);
            }
        }
    }

    /* =========================
       LISTAR CARRITO
       ========================= */
    public List<Carrito> obtenerCarrito(HttpSession session) {

        Usuario usuario = obtenerUsuarioLogueado();
        if (usuario == null) return new ArrayList<>();

        return repo.findByUsuarioId(usuario.getId());
    }

    /* =========================
       ELIMINAR PRODUCTO
       ========================= */
    @Transactional
    public void eliminarProducto(Long productoId, HttpSession session) {

        Usuario usuario = obtenerUsuarioLogueado();
        if (usuario == null) return;

        repo.deleteByUsuarioIdAndProductoId(
                usuario.getId(),
                productoId
        );
    }

    /* =========================
       LIMPIAR CARRITO
       ========================= */
    @Transactional
    public void limpiarCarrito(HttpSession session) {

        Usuario usuario = obtenerUsuarioLogueado();
        if (usuario == null) return;

        repo.deleteByUsuarioId(usuario.getId());
    }
}
