/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Tennis_ERP.TennisErp.Controller;

import Tennis_ERP.TennisErp.DAO.RolDAO;
import Tennis_ERP.TennisErp.Service.UsuarioService;
import Tennis_ERP.TennisErp.Validations.ValidationGroups.*;
import Tennis_ERP.TennisErp.Domain.Rol;
import Tennis_ERP.TennisErp.Domain.Usuario;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Set;

@Controller
public class JugadorController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private RolDAO rolDAO;

    @GetMapping("/menuAdmin/jugadores")
    public String listarJugadores(Model model) {
        List<Usuario> jugadores = usuarioService.getUsuariosPorRol("Jugador");
        model.addAttribute("jugadores", jugadores);
        return "jugadoresLista";
    }

    @GetMapping("/menuAdmin/jugadores/nuevo")
    public String mostrarFormularioNuevoJugador(Model model) {
        model.addAttribute("jugador", new Usuario());
        return "jugadoresCrear";
    }

    @PostMapping("/menuAdmin/jugadores/guardar")
    public String guardarJugador(
            @Validated(OnCreate.class) @ModelAttribute("jugador") Usuario jugador,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (usuarioService.findByNombreUsuario(jugador.getNombreUsuario()).isPresent()) {
            result.rejectValue("nombreUsuario", "error.jugador", "El nombre de usuario ya está en uso");
        }

        if (usuarioService.findByDni(jugador.getDni()).isPresent()) {
            result.rejectValue("dni", "error.jugador", "El DNI ya está registrado");
        }
        
        Rol rolJugador = rolDAO.findByNombreRol("Jugador");
        jugador.setRoles(Set.of(rolJugador));

        if (result.hasErrors()) {
            model.addAttribute("jugador", jugador);
            return "jugadoresCrear";
        }


        usuarioService.saveUsuario(jugador);
        redirectAttributes.addFlashAttribute("successMessage", "Jugador creado exitosamente.");

        return "redirect:/menuAdmin/jugadores";
    }

    @GetMapping("/menuAdmin/jugadores/editar/{id}")
    public String mostrarFormularioEditarJugador(@PathVariable Long id, Model model) {
        Usuario jugador = usuarioService.getUsuarioById(id).orElseThrow(() -> new IllegalArgumentException("ID no válido: " + id));
        model.addAttribute("jugador", jugador);
        return "jugadoresEditar";
    }

    @PostMapping("/menuAdmin/jugadores/actualizar/{id}")
    public String actualizarJugador(@PathVariable Long id,
            @Validated(OnUpdate.class) @ModelAttribute("jugador") Usuario jugador,
            BindingResult result,
            Model model) {

        if (result.hasErrors()) {
            return "jugadoresEditar";
        }

        Usuario existente = usuarioService.getUsuarioById(id).orElseThrow();
        if (!jugador.getEmail().equals(existente.getEmail()) && usuarioService.findByEmail(jugador.getEmail()).isPresent()) {
            result.rejectValue("email", "error.jugador", "El email ya está en uso");
        }

        if (!jugador.getNombreUsuario().equals(existente.getNombreUsuario()) && usuarioService.findByNombreUsuario(jugador.getNombreUsuario()).isPresent()) {
            result.rejectValue("nombreUsuario", "error.jugador", "El nombre de usuario ya está en uso");
        }

        if (result.hasErrors()) {
            return "jugadoresEditar";
        }

        Usuario original = usuarioService.getUsuarioById(jugador.getId())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con ID: " + jugador.getId()));

        jugador.setUsuarioCategorias(original.getUsuarioCategorias());
        jugador.setRoles(original.getRoles());

        if (jugador.getPassword() == null || jugador.getPassword().isBlank()) {
            jugador.setPassword(original.getPassword());
        } else {
            jugador.setPassword(usuarioService.encodePassword(jugador.getPassword()));
        }

        usuarioService.saveUsuario(jugador);

        return "redirect:/menuAdmin/jugadores";
    }

    @GetMapping("/menuAdmin/jugadores/eliminar/{id}")
    public String eliminarJugador(@PathVariable Long id) {
        usuarioService.deleteUsuario(id);
        return "redirect:/menuAdmin/jugadores";
    }
}
