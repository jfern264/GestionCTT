// Controller
package Tennis_ERP.TennisErp.Controller;

import Tennis_ERP.TennisErp.domain.Entrenador;
import Tennis_ERP.TennisErp.service.EntrenadorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/entrenadores")
public class EntrenadorController {

    @Autowired
    private EntrenadorService entrenadorService;

    @GetMapping("/adminentrenadores")
    public String mostrarEntrenadores(Model model) {
        List<Entrenador> entrenadores = entrenadorService.listarEntrenadores();
        model.addAttribute("entrenadores", entrenadores);
        return "AdminEntrenadores";
    }

    @GetMapping("/add")
    public String formularioNuevoEntrenador(Model model) {
        model.addAttribute("entrenador", new Entrenador());
        return "addEntrenador";
    }
    
    @PostMapping("/add")
    public String guardarEntrenador(@ModelAttribute Entrenador entrenador) {
        entrenadorService.crearEntrenador(entrenador);
        return "redirect:/entrenadores/adminentrenadores";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable("id") Long id, Model model) {
        Entrenador entrenador = entrenadorService.obtenerEntrenadorPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Entrenador no encontrado con ID: " + id));
        model.addAttribute("entrenador", entrenador);
        return "editar_entrenador";  // La vista debe corresponder con la plantilla correcta
    }


    @PostMapping("/editar")
    public String editarEntrenador(@ModelAttribute("entrenador") Entrenador entrenador) {
        entrenadorService.actualizarEntrenador(entrenador);
        return "redirect:/entrenadores/adminentrenadores";
    }


    
    
    @GetMapping("/eliminar/{id}")
    public String eliminarEntrenador(@PathVariable("id") Long id) {
        entrenadorService.eliminarEntrenador(id);
        return "redirect:/entrenadores/adminentrenadores";
    }
}