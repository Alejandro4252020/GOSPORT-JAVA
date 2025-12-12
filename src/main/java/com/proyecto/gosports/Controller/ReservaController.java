package com.proyecto.gosports.Controller;

import com.proyecto.gosports.model.Cancha;
import com.proyecto.gosports.model.Reserva;
import com.proyecto.gosports.model.Usuario;
import com.proyecto.gosports.repository.CanchaRepository;
import com.proyecto.gosports.service.ReservaService;
import com.proyecto.gosports.service.UsuarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/reservas")
public class ReservaController {

    @Autowired
    private CanchaRepository canchaRepository;

    @Autowired
    private ReservaService reservaService;

    @Autowired
    private UsuarioService usuarioService;

    // ----------------------------------------
    // 1. Mostrar formulario de reserva
    // ----------------------------------------
    @GetMapping("/nueva/{id}")
    public String mostrarFormularioReserva(@PathVariable("id") Long canchaId, Model model) {
        Optional<Cancha> opt = canchaRepository.findById(canchaId);
        if (opt.isEmpty()) {
            return "redirect:/canchas?error=canchaNoEncontrada";
        }
        model.addAttribute("cancha", opt.get());
        return "reservar-cancha"; 
    }

    // ----------------------------------------
    // 2. Guardar reserva
    // ----------------------------------------
    @PostMapping("/guardar")
    public String guardarReserva(
            @RequestParam Long canchaId,
            @RequestParam String clienteUsername,
            @RequestParam String telefono,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fecha,
            @RequestParam @DateTimeFormat(pattern = "HH:mm") LocalTime hora) {

        LocalTime apertura = LocalTime.of(6, 0);
        LocalTime cierre = LocalTime.of(22, 0);

        if (hora.isBefore(apertura) || hora.isAfter(cierre)) {
            return "redirect:/canchas/" + canchaId + "?error=horarioNoValido";
        }

        try {
            // Usar método que devuelve Usuario directamente
            Usuario usuario = usuarioService.getByUserName(clienteUsername);

            reservaService.crearReserva(canchaId, usuario.getId(), telefono, fecha, hora);

        } catch (IllegalArgumentException iae) {
            return "redirect:/canchas/" + canchaId + "?error=" + iae.getMessage();
        } catch (Exception ex) {
            return "redirect:/canchas/" + canchaId + "?error=errorInterno";
        }

        return "redirect:/canchas/" + canchaId + "?reserva=success";
    }

    // ----------------------------------------
    // 3. Historial de reservas del usuario logueado
    // ----------------------------------------
    @GetMapping("/historial")
public String historial(Principal principal, Model model) {
    if (principal == null) {
        return "redirect:/login";
    }

    // Obtener el usuario por username en vez de email
    Usuario cliente = usuarioService.getByUserName(principal.getName());

    List<Reserva> reservas = reservaService.historialPorCliente(cliente.getId());
    model.addAttribute("reservas", reservas);

        return "/historial";
    }

    // 4. Mostrar formulario para editar reserva
    @GetMapping("/editar/{id}")
    public String editarReserva(@PathVariable Long id, Principal principal, Model model) {
        Reserva reserva = reservaService.getReserva(id);
        Usuario cliente = usuarioService.getByUserName(principal.getName());

        // Solo el dueño puede editar
        if (!reserva.getCliente().getId().equals(cliente.getId())) {
            return "redirect:/reservas/historial";
        }

        model.addAttribute("reserva", reserva);
        model.addAttribute("canchas", canchaRepository.findAll());
        return "reservas/editar";
    }

    // 5. Guardar cambios de reserva editada
    @PostMapping("/editar/{id}")
    public String actualizarReserva(@PathVariable Long id, @ModelAttribute Reserva reservaForm, Principal principal) {
        Reserva reserva = reservaService.getReserva(id);
        Usuario cliente = usuarioService.getByUserName(principal.getName());

        if (!reserva.getCliente().getId().equals(cliente.getId())) {
            return "redirect:/reservas/historial";
        }

        // Actualizar solo campos permitidos
        reserva.setFecha(reservaForm.getFecha());
        reserva.setHora(reservaForm.getHora());
        reserva.setTelefono(reservaForm.getTelefono());
        reserva.setCancha(reservaForm.getCancha());

        reservaService.actualizarReserva(reserva);
        return "redirect:/reservas/historial";
    }

}
