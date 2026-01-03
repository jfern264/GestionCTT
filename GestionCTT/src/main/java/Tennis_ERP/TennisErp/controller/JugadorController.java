/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Tennis_ERP.TennisErp.controller;

import Tennis_ERP.TennisErp.service.EquipoServiceImpl;
import Tennis_ERP.TennisErp.service.RolService;
import Tennis_ERP.TennisErp.service.UsuarioService;
import Tennis_ERP.TennisErp.validations.ValidationGroups.*;
import Tennis_ERP.TennisErp.service.EquipoService;
import Tennis_ERP.TennisErp.validations.ValidationGroups.*;
import Tennis_ERP.TennisErp.dao.RolDAO;
import Tennis_ERP.TennisErp.domain.Rol;
import Tennis_ERP.TennisErp.domain.Usuario;
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

    @Autowired
    private RolService rolService;

    @Autowired
    private EquipoService equipoService;

    @GetMapping("/jugadores")
    public String listarParaUsuarios(Model model) {
        // Obtenemos la lista de jugadores
        List<Usuario> jugadores = usuarioService.getUsuariosPorRol("ROLE_JUGADOR");
        model.addAttribute("jugadores", jugadores);
        // Puedes usar un HTML distinto (jugadoresPublico.html) o el mismo
        return "jugadoresLista";
    }

    @GetMapping("/equipos")
    public String listarEquiposPublico(Model model) {
        // CAMBIA ESTO: model.addAttribute("equipos", equipoService.getAllEquipos());
        // POR ESTO:
        model.addAttribute("equipos", equipoService.findAll());
        return "equiposLista";
    }

    @GetMapping("/menuAdmin/jugadores")
    public String listarJugadores(Model model) {
        // CAMBIA "Jugador" por "ROLE_JUGADOR"
        List<Usuario> jugadores = usuarioService.getUsuariosPorRol("ROLE_JUGADOR");
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

        // Validaciones
        if (usuarioService.findByNombreUsuario(jugador.getNombreUsuario()).isPresent()) {
            result.rejectValue("nombreUsuario", "error.jugador", "El nombre de usuario ya está en uso");
        }

        if (usuarioService.findByDni(jugador.getDni()).isPresent()) {
            result.rejectValue("dni", "error.jugador", "El DNI ya está registrado");
        }

        if (result.hasErrors()) {
            return "jugadoresCrear";
        }

        // Asignar rol usando el servicio
        Rol rolJugador = rolService.findByNombreRol("ROLE_JUGADOR"); // devuelve Rol directamente
        jugador.setRoles(Set.of(rolJugador));

        // Encriptar contraseña
        jugador.setPassword(usuarioService.encodePassword(jugador.getPassword()));

        // Guardar jugador
        usuarioService.saveUsuario(jugador);

        redirectAttributes.addFlashAttribute("successMessage", "Jugador creado exitosamente.");
        return "redirect:/menuAdmin/jugadores";
    }


    @GetMapping("/menuAdmin/jugadores/editar/{id}")
    public String mostrarFormularioEditarJugador(@PathVariable Long id, Model model) {
        Usuario jugador = usuarioService.getUsuarioById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID no válido: " + id));
        model.addAttribute("jugador", jugador);
        return "jugadoresEditar";
    }

    @PostMapping("/menuAdmin/jugadores/actualizar/{id}")
    public String actualizarJugador(@PathVariable Long id,
            @Validated(OnUpdate.class) @ModelAttribute("jugador") Usuario jugador,
            BindingResult result,
            Model model) {


        Usuario existente = usuarioService.getUsuarioById(id).orElseThrow();
        if (!jugador.getEmail().equals(existente.getEmail())
                && usuarioService.findByEmail(jugador.getEmail()).isPresent()) {
            result.rejectValue("email", "error.jugador", "El email ya está en uso");
        }

        if (!jugador.getNombreUsuario().equals(existente.getNombreUsuario())
                && usuarioService.findByNombreUsuario(jugador.getNombreUsuario()).isPresent()) {
            result.rejectValue("nombreUsuario", "error.jugador", "El nombre de usuario ya está en uso");
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

    @GetMapping("/menuAdmin/jugadoresDebug")
        public String listarJugadoresDebug(Model model) {
            // Obtener todos los roles
            List<Rol> roles = rolDAO.findAll();

            // Imprimir en consola
            System.out.println("===== Roles en la base de datos =====");
            roles.forEach(r -> System.out.println("'" + r.getNombreRol() + "'"));
            System.out.println("===================================");

            // Pasar los roles al modelo para mostrarlos en la página
            model.addAttribute("roles", roles);

            return "debugRoles"; // nombre de la plantilla Thymeleaf
        }

}
