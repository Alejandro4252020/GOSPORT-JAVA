package com.proyecto.gosports.Controller;

import com.proyecto.gosports.model.Usuario;
import com.proyecto.gosports.repository.UsuarioRepository;
import com.proyecto.gosports.service.ReporteService;
import jakarta.persistence.criteria.Predicate;
import jakarta.servlet.http.HttpServletResponse;
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

    @GetMapping("/home")
    public String home(Model model, Authentication auth) {
        model.addAttribute("rol", auth.getAuthorities().toString());
        return "home";
    }

    // -----------------------------
    // CRUD SOLO ADMIN
    // -----------------------------
    @GetMapping("/usuarios")
    public String listar(Model model) {
        model.addAttribute("usuarios", repo.findAll());
        return "usuarios";
    }

    // -----------------------------
    // 📌 MÉTODO DE BÚSQUEDA
    // -----------------------------
    @GetMapping("/usuarios/buscar")
    public String buscar(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String rol,
            Model model) {

        List<Usuario> lista = repo.findAll((root, query, cb) -> {

            List<Predicate> filtros = new ArrayList<>();

            if (id != null) {
                filtros.add(cb.equal(root.get("id"), id));
            }

            if (username != null && !username.isEmpty()) {
                filtros.add(cb.like(cb.lower(root.get("userName")),
                        "%" + username.toLowerCase() + "%"));
            }

            if (rol != null && !rol.isEmpty()) {
                filtros.add(cb.equal(root.get("rol"), rol));
            }

            return cb.and(filtros.toArray(new Predicate[0]));
        });

        model.addAttribute("usuarios", lista);
        return "usuarios";
    }

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
    // PERFIL DEL USUARIO (USER)
    // -----------------------------
    @GetMapping("/perfil")
    public String perfil(Model model, Authentication auth) {
        String username = auth.getName();
        Usuario usuario = repo.findByUserName(username).orElseThrow();
        model.addAttribute("usuario", usuario);
        return "form";
    }

    @PostMapping("/perfil/guardar")
    public String guardarPerfil(@ModelAttribute Usuario usuario, Authentication auth) {
        String username = auth.getName();
        Usuario actual = repo.findByUserName(username).orElseThrow();

        actual.setUserName(usuario.getUserName());
        actual.setPassword(new BCryptPasswordEncoder().encode(usuario.getPassword()));
        repo.save(actual);

        return "redirect:/home?actualizado";
    }

    @GetMapping("/paginaprincipal")
    public String paginaPrincipal() {
        return "paginaprincipal";
    }

    // -----------------------------
    // REPORTES CON FILTROS MULTICRITERIO
    // -----------------------------

    @Autowired
    private ReporteService reporteService;

    @GetMapping("/usuarios/reporte/pdf")
    public void reportePdf(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String rol,
            HttpServletResponse response) throws Exception {

        List<Usuario> lista = repo.findAll((root, query, cb) -> {
            List<Predicate> filtros = new ArrayList<>();

            if (id != null) filtros.add(cb.equal(root.get("id"), id));
            if (username != null && !username.isEmpty())
                filtros.add(cb.like(cb.lower(root.get("userName")), "%" + username.toLowerCase() + "%"));
            if (rol != null && !rol.isEmpty())
                filtros.add(cb.equal(root.get("rol"), rol));

            return cb.and(filtros.toArray(new Predicate[0]));
        });

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=usuarios.pdf");

        reporteService.generarReportePdf(lista, response.getOutputStream());
    }

    @GetMapping("/usuarios/reporte/excel")
    public void reporteExcel(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String rol,
            HttpServletResponse response) throws Exception {

        List<Usuario> lista = repo.findAll((root, query, cb) -> {
            List<Predicate> filtros = new ArrayList<>();

            if (id != null) filtros.add(cb.equal(root.get("id"), id));
            if (username != null && !username.isEmpty())
                filtros.add(cb.like(cb.lower(root.get("userName")), "%" + username.toLowerCase() + "%"));
            if (rol != null && !rol.isEmpty())
                filtros.add(cb.equal(root.get("rol"), rol));

            return cb.and(filtros.toArray(new Predicate[0]));
        });

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=usuarios.xlsx");

        reporteService.generarReporteExcel(lista, response.getOutputStream());
    }

    @GetMapping("/usuarios/reporte/plano")
    public void reportePlano(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String rol,
            HttpServletResponse response) throws Exception {

        List<Usuario> lista = repo.findAll((root, query, cb) -> {
            List<Predicate> filtros = new ArrayList<>();

            if (id != null) filtros.add(cb.equal(root.get("id"), id));
            if (username != null && !username.isEmpty())
                filtros.add(cb.like(cb.lower(root.get("userName")), "%" + username.toLowerCase() + "%"));
            if (rol != null && !rol.isEmpty())
                filtros.add(cb.equal(root.get("rol"), rol));

            return cb.and(filtros.toArray(new Predicate[0]));
        });

        response.setContentType("text/plain");
        response.setHeader("Content-Disposition", "attachment; filename=usuarios.txt");

        reporteService.generarReportePlano(lista, response.getOutputStream());
    }
}
