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

    @GetMapping("/jugadores")
    public String mostrarJugadores(Model model) {
        List<Jugadores> listaJugadores = jugadoresService.findAllJugadores();
        model.addAttribute("jugadores", listaJugadores);
        return "Jugadores";
    }

    @GetMapping("/adminjugadores")
    public String AdministrarJugadores(Model model) {
        List<Jugadores> listaJugadores = jugadoresService.findAllJugadores();
        model.addAttribute("jugadores", listaJugadores);
        return "AdminJugadores";
    }

    @GetMapping("/adminjugadores/addJugador")
    public String mostrarFormularioAgregarJugador(Model model) {
        prepararFormulario(model, new Jugadores());
        return "addJugador";
    }

    @PostMapping("/adminjugadores/addJugadores")
    public String guardarJugador(@ModelAttribute Jugadores jugador, BindingResult result, RedirectAttributes redirectAttributes, Model model) {
        if (result.hasErrors()) {
            prepararFormulario(model, jugador);
            return "addJugador";
        }

        try {
            rol rolSeleccionado = rolService.findRolById(jugador.getRol().getId());
            if (rolSeleccionado == null) {
                redirectAttributes.addFlashAttribute("error", "Rol no encontrado");
                return "redirect:/adminjugadores/addJugador";
            }

            jugador.setRol(rolSeleccionado);
            jugadoresService.saveJugador(jugador);
            redirectAttributes.addFlashAttribute("success", "Jugador añadido con éxito");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ocurrió un error inesperado");
            return "redirect:/adminjugadores/addJugador";
        }

        return "redirect:/adminjugadores";
    }

    private void prepararFormulario(Model model, Jugadores jugador) {
        List<rol> listaRoles = rolService.findAllRoles();
        model.addAttribute("roles", listaRoles);
        model.addAttribute("jugador", jugador);
    }

    @GetMapping("/adminjugadores/editJugador/{id}")
    public String mostrarFormularioEditarJugador(@PathVariable Long id, Model model) {
        Optional<Jugadores> jugadorOpt = jugadoresService.findJugadorById(id);
        if (!jugadorOpt.isPresent()) {
            model.addAttribute("error", "Jugador no encontrado");
            return "errorPage";
        }

        Jugadores jugador = jugadorOpt.get();
        List<rol> listaRoles = rolService.findAllRoles();

        model.addAttribute("jugador", jugador);
        model.addAttribute("roles", listaRoles);

        return "editar_Jugador";
    }

    @PostMapping("/adminjugadores/editJugador/{id}")
    public String editarJugador(@PathVariable Long id, @ModelAttribute Jugadores jugador, Model model) {
        try {

            Optional<Jugadores> jugadorExistenteOpt = jugadoresService.findJugadorById(id);
            if (!jugadorExistenteOpt.isPresent()) {
                throw new EntityNotFoundException("Jugador no encontrado");
            }

            Jugadores jugadorExistente = jugadorExistenteOpt.get();
            jugador.setId(id);
            jugador.setRol(jugadorExistente.getRol());

            jugadoresService.saveJugador(jugador);

        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "errorPage";
        } catch (Exception e) {
            model.addAttribute("error", "Ocurrió un error inesperado");
            return "errorPage";
        }

        return "redirect:/adminjugadores";
    }

    @GetMapping("/adminjugadores/deleteJugador/{id}")
    public String eliminarJugador(@PathVariable Long id, Model model) {
        try {
            jugadoresService.deleteJugador(id);
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "errorPage";
        } catch (Exception e) {
            model.addAttribute("error", "Ocurrió un error inesperado");
            return "errorPage";
        }
        return "redirect:/adminjugadores";
    }
}
