package com.proyecto.gosports.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    // Mostrar la página de "Olvidé mi contraseña"
    @GetMapping("/forgot-password")
    public String forgotPassword() {
        return "forgot-password"; // Thymeleaf buscará forgot-password.html en templates
    }

    // Procesar el formulario de "Olvidé mi contraseña"
    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam("email") String email, Model model) {
        // Aquí puedes implementar la lógica para enviar un correo con enlace de recuperación
        model.addAttribute("mensajeExito", "Se envió un enlace de recuperación a: " + email);
        return "forgot-password";
    }
}