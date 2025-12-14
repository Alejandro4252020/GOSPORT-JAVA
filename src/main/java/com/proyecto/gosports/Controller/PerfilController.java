package com.proyecto.gosports.Controller;

import com.proyecto.gosports.model.Usuario;
import com.proyecto.gosports.security.CustomUserDetails;
import com.proyecto.gosports.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;

@Controller
@RequestMapping("/mi-perfil")
public class PerfilController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final Path uploadPath = Paths.get("uploads");

    @GetMapping
    public String verPerfil(
            Model model,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {

        if (userDetails == null) {
            return "redirect:/login";
        }

        Usuario usuario = userDetails.getUsuario();

        model.addAttribute("usuario", usuario);
        return "perfil";
    }

        @PostMapping("/editar")
    public String editarPerfil(
            @RequestParam("username") String username,
            @RequestParam(value = "password", required = false) String password,
            @RequestParam(value = "foto", required = false) MultipartFile foto,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) throws IOException {
    
        if (userDetails == null) {
            return "redirect:/login";
        }
    
        Usuario usuario = userDetails.getUsuario();
    
        // Detectamos si el username cambió
        boolean cambioUsername = !usuario.getUsername().equals(username);
    
        // Actualizamos los detalles del usuario
        usuario.setUsername(username);
    
        if (password != null && !password.isBlank()) {
            usuario.setPassword(passwordEncoder.encode(password));
        }
    
        if (foto != null && !foto.isEmpty()) {
            // Creamos directorio si no existe
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
        
            String fotoNombre = System.currentTimeMillis() + "_" + foto.getOriginalFilename();
            Path ruta = uploadPath.resolve(fotoNombre);
        
            Files.copy(foto.getInputStream(), ruta, StandardCopyOption.REPLACE_EXISTING);
            usuario.setFotoPerfil(fotoNombre);
        }
    
        usuarioRepository.save(usuario);
    
        // Si el nombre de usuario cambió, forzamos logout
        if (cambioUsername) {
            return "redirect:/login?logout";

        }
    
        return "redirect:/mi-perfil?success";
    }
}
