package Tennis_ERP.TennisErp.Controller;

import Tennis_ERP.TennisErp.Service.EquipoService;
import Tennis_ERP.TennisErp.domain.Equipo;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;





/*Para añadir nuevo equipo /equipos/nuevo */




@Controller
public class EquipoController {

    @Autowired
    private EquipoService equipoService;

    // Mostrar todos los equipos
    @GetMapping("/equipos")
    public String mostrarEquipos(Model model) {
        List<Equipo> equipos = equipoService.listarEquipos();
        model.addAttribute("equipos", equipos);
        return "equipo"; // Vista que muestra todos los equipos
    }

    @GetMapping("/equipo_categoria/{categoria}")
    public String mostrarCategoria(@PathVariable String categoria, Model model) {
        List<Equipo> equipos = equipoService.listarEquipos();
        model.addAttribute("equipos", equipos);
        return "equipo_categoria/" + categoria;
    }

    // Mostrar formulario para añadir un nuevo equipo
    @GetMapping("/equipos/nuevo")
    public String formularioNuevoEquipo(Model model) {
        model.addAttribute("equipo", new Equipo()); // Inicializa un equipo vacío para el formulario
        return "addEquipo"; // Vista para agregar un nuevo equipo
    }

    // Guardar un nuevo equipo
    @PostMapping("/equipos/guardar")
    public String guardarEquipo(@ModelAttribute Equipo equipo) {
        equipoService.crearEquipo(equipo); // El ID no debería ser enviado en el formulario
        return "redirect:/equipos"; // Redirige a la lista de equipos después de guardar
    }

    // Mostrar el formulario de edición
    @GetMapping("/equipos/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable("id") Long id, Model model) {
        Equipo equipo = equipoService.obtenerEquipoPorId(id).orElseThrow(() -> 
                new IllegalArgumentException("El equipo con ID " + id + " no existe."));
        model.addAttribute("equipo", equipo);
        return "editar_equipo"; // Vista para el formulario de edición
    }

    // Procesar la edición de un equipo
    @PostMapping("/equipos/editar")
    public String editarEquipo(@ModelAttribute("equipo") Equipo equipo) {
        equipoService.actualizarEquipo(equipo);
        return "redirect:/equipos";
    }

    // Eliminar un equipo por su ID
    @GetMapping("/equipos/eliminar/{id}")
    public String eliminarEquipo(@PathVariable("id") Long id) {
        equipoService.eliminarEquipo(id);
        return "redirect:/equipos";
    }
}
