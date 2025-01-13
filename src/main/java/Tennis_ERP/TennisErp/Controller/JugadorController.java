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
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @GetMapping("/adminjugadores")
    public String AdministrarJugadores(Model model) {
        List<Jugadores> listaJugadores = jugadoresService.findAllJugadores();
        model.addAttribute("jugadores", listaJugadores);
        return "AdminJugadores";
    }

    // Mostrar formulario para añadir un nuevo jugador
    @GetMapping("/addJugador")
    public String mostrarFormularioAgregarJugador(Model model) {
        prepararFormulario(model, new Jugadores());
        return "addJugador";
    }

    @PostMapping("/addJugadores")
    public String guardarJugador(@ModelAttribute Jugadores jugador, BindingResult result, RedirectAttributes redirectAttributes, Model model) {
        if (result.hasErrors()) {
            prepararFormulario(model, jugador);
            return "addJugador";
        }

        try {
            rol rolSeleccionado = rolService.findRolById(jugador.getRol().getId());
            if (rolSeleccionado == null) {
                redirectAttributes.addFlashAttribute("error", "Rol no encontrado");
                return "redirect:/addJugador";
            }

            jugador.setRol(rolSeleccionado);
            jugadoresService.saveJugador(jugador);
            redirectAttributes.addFlashAttribute("success", "Jugador añadido con éxito");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ocurrió un error inesperado");
            return "redirect:/addJugador";
        }

        return "redirect:/adminjugadores";
    }

    private void prepararFormulario(Model model, Jugadores jugador) {
        List<rol> listaRoles = rolService.findAllRoles();
        model.addAttribute("roles", listaRoles);
        model.addAttribute("jugador", jugador);
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
        return "redirect:/adminjugadores";
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
        return "redirect:/adminjugadores";
    }
}
