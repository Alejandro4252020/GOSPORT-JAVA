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

    @GetMapping("/home")
    public String home(Authentication auth, Model model) {
        if (auth != null) {
            model.addAttribute("rol", auth.getAuthorities().toString());
        }
        return "home";
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
    public String guardar(@ModelAttribute Usuario usuario) {
        usuario.setPassword(new BCryptPasswordEncoder().encode(usuario.getPassword()));
        repo.save(usuario);
        return "redirect:/usuarios";
    }

    @GetMapping("/usuarios/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("usuario", repo.findById(id).orElseThrow());
        return "form";
    }

    @GetMapping("/usuarios/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        repo.deleteById(id);
        return "redirect:/usuarios";
    }

    // -----------------------------
    // PERFIL DE USUARIO
    // -----------------------------
    @GetMapping("/perfil")
    public String perfil(Model model, Authentication auth) {
        if (auth != null) {
            String username = auth.getName();
            Usuario usuario = repo.findByUserName(username).orElseThrow();
            model.addAttribute("usuario", usuario);
        }
        return "perfil";
    }

    @PostMapping("/perfil/guardar")
    public String guardarPerfil(@ModelAttribute Usuario usuario, Authentication auth) {
        if (auth != null) {
            String username = auth.getName();
            Usuario actual = repo.findByUserName(username).orElseThrow();
            actual.setUserName(usuario.getUserName());
            actual.setPassword(new BCryptPasswordEncoder().encode(usuario.getPassword()));
            repo.save(actual);
        }
        return "redirect:/perfil?actualizado";
    }
}
