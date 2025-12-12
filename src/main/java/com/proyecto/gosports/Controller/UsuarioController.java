package com.proyecto.gosports.Controller;

import com.proyecto.gosports.model.Usuario;
import com.proyecto.gosports.repository.UsuarioRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class UsuarioController {

    @Autowired
    private UsuarioRepository repo;

    // -----------------------------
    // REDIRECCIONES GENERALES  
    // -----------------------------
    @GetMapping("/")
    public String redireccionRaiz() {
        return "redirect:/paginaprincipal";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/paginaprincipal")
    public String paginaPrincipal(Authentication auth, Model model) {
        if (auth != null) {
            model.addAttribute("rol", auth.getAuthorities().toString());
        }
        return "paginaprincipal";
    }


    // -----------------------------
    // LISTAR USUARIOS (ADMIN)
    // -----------------------------
    @GetMapping("/usuarios")
    public String listar(Model model) {
        model.addAttribute("usuarios", repo.findAll());
        return "usuarios";
    }

    // -----------------------------
    // BÚSQUEDA MULTICRITERIO
    // -----------------------------
    @GetMapping("/usuarios/buscar")
    public String buscar(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String rol,
            Model model
    ) {
        List<Usuario> lista = repo.findAll((root, query, cb) -> {
            List<Predicate> filtros = new ArrayList<>();

            if (id != null) {
                filtros.add(cb.equal(root.get("id"), id));
            }

            if (username != null && !username.isEmpty()) {
                filtros.add(cb.like(cb.lower(root.get("userName")), "%" + username.toLowerCase() + "%"));
            }

            if (rol != null && !rol.isEmpty()) {
                filtros.add(cb.equal(root.get("rol"), rol));
            }

            return cb.and(filtros.toArray(new Predicate[0]));
        });

        model.addAttribute("usuarios", lista);
        return "usuarios";
    }

    // -----------------------------
    // CRUD ADMIN
    // -----------------------------
    @GetMapping("/usuarios/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "form";
    }

    @PostMapping("/usuarios/guardar")
    public String guardar(@ModelAttribute Usuario usuario, Model model) {
    
        // Verificar si ya existe un usuario con ese nombre (excepto si es la edición del mismo)
        if (usuario.getId() == null) { // nuevo usuario
            if (repo.findByUserName(usuario.getUserName()).isPresent()) {
                model.addAttribute("mensajeError", "El nombre de usuario ya existe.");
                return "form"; // vuelve al formulario
            }
            if (repo.findByEmail(usuario.getEmail()).isPresent()) {
                model.addAttribute("mensajeError", "El correo electrónico ya está registrado.");
                return "form"; 
            }
        } else { // edición de usuario
            Usuario existente = repo.findByUserName(usuario.getUserName())
                    .filter(u -> !u.getId().equals(usuario.getId()))
                    .orElse(null);
            if (existente != null) {
                model.addAttribute("mensajeError", "El nombre de usuario ya existe.");
                return "form";
            }
            Usuario emailExistente = repo.findByEmail(usuario.getEmail())
                    .filter(u -> !u.getId().equals(usuario.getId()))
                    .orElse(null);
            if (emailExistente != null) {
                model.addAttribute("mensajeError", "El correo electrónico ya está registrado.");
                return "form";
            }
        }
    
        // Guardar usuario con contraseña encriptada
        usuario.setPassword(new BCryptPasswordEncoder().encode(usuario.getPassword()));
        repo.save(usuario);
    
        model.addAttribute("mensajeExito", "Usuario guardado correctamente.");
        return "redirect:/usuarios";
    }


}
