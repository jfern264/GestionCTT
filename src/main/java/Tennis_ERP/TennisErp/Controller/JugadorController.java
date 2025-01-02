package Tennis_ERP.TennisErp.controller;

import Tennis_ERP.TennisErp.domain.Jugadores;
import Tennis_ERP.TennisErp.domain.rol;
import Tennis_ERP.TennisErp.service.JugadoresService;
import Tennis_ERP.TennisErp.service.RolService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@Controller
public class JugadorController {

    @Autowired
    private JugadoresService jugadoresService;

    @Autowired
    private RolService rolService;

    // Mostrar la página principal de jugadores
    @GetMapping("/jugadores")
    public String mostrarJugadores(Model model) {
        List<Jugadores> listaJugadores = jugadoresService.findAllJugadores();
        model.addAttribute("jugadores", listaJugadores);
        return "Jugadores"; // Nombre de la plantilla HTML
    }

    // Mostrar formulario para añadir un nuevo jugador
    @GetMapping("/addJugador")
    public String mostrarFormularioNuevoJugador(Model model) {
        // Obtener todos los roles
        List<rol> listaRoles = rolService.findAllRoles();

        // Pasar los roles a la vista y el objeto vacío para el jugador
        model.addAttribute("roles", listaRoles);
        model.addAttribute("jugador", new Jugadores());

        // Retornar el nombre de la vista que contiene el formulario
        return "addJugador";
    }

    // Manejar el envío del formulario para añadir un nuevo jugador
    @PostMapping("/addJugadores")
    public String agregarNuevoJugador(@ModelAttribute Jugadores jugador, Model model) {
        try {
            // Verificamos si el rol seleccionado existe
            rol rolSeleccionado = rolService.findRolById(jugador.getRol().getId());  // Obtener el rol del jugador
            if (rolSeleccionado == null) {
                throw new EntityNotFoundException("Rol no encontrado");
            }

            jugador.setRol(rolSeleccionado); // Asignar el rol al jugador
            jugadoresService.saveJugador(jugador); // Guardar el jugador

        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "addJugador"; // Volver al formulario con un mensaje de error
        } catch (Exception e) {
            model.addAttribute("error", "Ocurrió un error inesperado");
            return "addJugador"; // Volver al formulario con un mensaje de error
        }

        // Redirigir a la lista de jugadores
        return "redirect:/jugadores";
    }

    @GetMapping("/editJugador/{id}")
    public String mostrarFormularioEditarJugador(@PathVariable Long id, Model model) {
        Optional<Jugadores> jugadorOpt = jugadoresService.findJugadorById(id);
        if (!jugadorOpt.isPresent()) {
            model.addAttribute("error", "Jugador no encontrado");
            return "errorPage";  // Página de error si no se encuentra el jugador
        }

        Jugadores jugador = jugadorOpt.get();  // Obtener el jugador
        List<rol> listaRoles = rolService.findAllRoles();  // Obtener todos los roles

        model.addAttribute("jugador", jugador);  // Agregar el jugador al modelo
        model.addAttribute("roles", listaRoles);  // Agregar la lista de roles al modelo

        return "editar_Jugador";  // Nombre de la vista (editar jugador)
    }

    @PostMapping("/editJugador/{id}")
    public String editarJugador(@PathVariable Long id, @ModelAttribute Jugadores jugador, Model model) {
        try {
            // Verificamos si el jugador existe
            Optional<Jugadores> jugadorExistenteOpt = jugadoresService.findJugadorById(id);
            if (!jugadorExistenteOpt.isPresent()) {
                throw new EntityNotFoundException("Jugador no encontrado");
            }

            Jugadores jugadorExistente = jugadorExistenteOpt.get();
            jugador.setId(id); // Aseguramos que el ID no cambie
            jugador.setRol(jugadorExistente.getRol()); // Mantener el rol del jugador original (si no lo editamos)

            jugadoresService.saveJugador(jugador); // Guardamos los cambios

        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "errorPage"; // Página de error si no se puede encontrar el jugador
        } catch (Exception e) {
            model.addAttribute("error", "Ocurrió un error inesperado");
            return "errorPage"; // Página de error genérica
        }

        // Redirigimos a la lista de jugadores
        return "redirect:/jugadores";
    }

    // Eliminar un jugador
    @GetMapping("/deleteJugador/{id}")
    public String eliminarJugador(@PathVariable Long id, Model model) {
        try {
            jugadoresService.deleteJugador(id);
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "errorPage"; // Página de error si no se puede eliminar el jugador
        } catch (Exception e) {
            model.addAttribute("error", "Ocurrió un error inesperado");
            return "errorPage"; // Página de error genérica
        }
        return "redirect:/jugadores";
    }
}
