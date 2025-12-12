package com.proyecto.gosports.Controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.proyecto.gosports.model.Usuario;
import com.proyecto.gosports.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

@Controller
public class RegistroController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public RegistroController(UsuarioRepository usuarioRepository,
                              PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Mostrar formulario
    @GetMapping("/registrar")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registrar"; // <-- tu archivo registrar.html
    }

    // Procesar formulario
    @PostMapping("/registrar")
    public String registrarUsuario(@ModelAttribute Usuario usuario, Model model) {

        // Validar si ya existe el username
        if (usuarioRepository.findByUserName(usuario.getUserName()).isPresent()) {
            model.addAttribute("error", "El nombre de usuario ya está en uso");
            return "registrar";
        }

        // Asignar rol por defecto
        usuario.setRol("USER");

        // Encriptar contraseña
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

        // Guardar
        usuarioRepository.save(usuario);

        return "redirect:/login?registrado";
    }
}
