package com.proyecto.gosports.Controller;

import com.proyecto.gosports.model.Usuario;
import com.proyecto.gosports.repository.UsuarioRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class CatalogoController {

    private final UsuarioRepository usuarioRepository;

    public CatalogoController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping("/catalogo")
    public String catalogo(Model model, Principal principal) {

        if (principal != null) {
            String username = principal.getName();  // obtiene el usuario logueado
            Usuario usuario = usuarioRepository.findByUserName(username).orElse(null);

            model.addAttribute("usuario", usuario);  // aquí lo mandamos al HTML
        }

        return "catalogo";
    }
}
