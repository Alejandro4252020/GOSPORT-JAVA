package com.proyecto.gosports.Controller;

import com.proyecto.gosports.model.Usuario;
import com.proyecto.gosports.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.security.Principal;

@Controller
@RequestMapping("/mi-perfil")
public class PerfilController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // ← Usamos el bean de SecurityConfig

    private final Path uploadPath = Paths.get("uploads"); // Carpeta para fotos

    @GetMapping
    public String verPerfil(Model model, Principal principal) {
        Usuario usuario = usuarioRepository.findByUserName(principal.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        model.addAttribute("usuario", usuario);
        return "perfil";
    }

    @PostMapping("/editar")
    public String editarPerfil(
            @RequestParam("userName") String userName,
            @RequestParam("password") String password,
            @RequestParam("foto") MultipartFile foto,
            Principal principal
    ) throws IOException {

        Usuario usuario = usuarioRepository.findByUserName(principal.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Actualizamos nombre
        usuario.setUserName(userName);

        // Actualizamos contraseña solo si se ingresa
        if (password != null && !password.isEmpty()) {
            usuario.setPassword(passwordEncoder.encode(password));
        }

        // Actualizamos foto si se sube una nueva
        if (foto != null && !foto.isEmpty()) {
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            String fotoNombre = System.currentTimeMillis() + "_" + foto.getOriginalFilename();
            Path ruta = uploadPath.resolve(fotoNombre);
            Files.copy(foto.getInputStream(), ruta, StandardCopyOption.REPLACE_EXISTING);
            usuario.setFotoPerfil(fotoNombre);
        }

        usuarioRepository.save(usuario);

        // Redirigimos a la misma página con parámetro success
        return "redirect:/mi-perfil?success";
    }
}
