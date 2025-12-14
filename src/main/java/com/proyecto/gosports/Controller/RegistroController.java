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

    @GetMapping("/registrar")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registrar";
    }

    @PostMapping("/registrar")
    public String registrarUsuario(@ModelAttribute Usuario usuario, Model model) {

        if (usuarioRepository.findByUsername(usuario.getUsername()).isPresent()) {
            model.addAttribute("error", "El nombre de usuario ya está en uso");
            return "registrar";
        }

        if (!passwordSegura(usuario.getPassword())) {
            model.addAttribute(
                "error",
                "La contraseña debe tener mínimo 8 caracteres, una mayúscula, una minúscula, un número y un carácter especial."
            );
            return "registrar";
        }

        usuario.setRol("USER");
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuarioRepository.save(usuario);

        return "redirect:/login?registrado";
    }

    // 🔐 MÉTODO FALTANTE
    private boolean passwordSegura(String password) {
        return password != null && password.matches(
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&]).{8,}$"
        );
    }
}

