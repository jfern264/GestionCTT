package Tennis_ERP.TennisErp.Controller;

import Tennis_ERP.TennisErp.Service.EquipoService;
import Tennis_ERP.TennisErp.domain.Equipo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public String findEquiposByNombreCategoria(@PathVariable String categoria, Model model) {
        String categoriaNormalizada = categoria.replace("_", " ");
        List<Equipo> equipos = equipoService.findEquiposByNombreCategoria(categoriaNormalizada);
        model.addAttribute("equipos", equipos);
        return "equipo_categoria/"+ categoria; // Asegúrate de que esta vista existe
    }


    

    // Mostrar formulario para añadir un nuevo equipo
    @GetMapping("/equipos/nuevo")
    public String formularioNuevoEquipo(Model model) {
        model.addAttribute("equipo", new Equipo());
        
        return "addEquipo"; // Vista para agregar un nuevo equipo
    }

    // Guardar un nuevo equipo
    @PostMapping("/equipos/guardar")
    public String guardarEquipo(@ModelAttribute Equipo equipo) {
        equipoService.crearEquipo(equipo);
        return "redirect:/equipos"; // Redirige a la lista de equipos después de guardar
    }

    // Mostrar el formulario de edición
    @GetMapping("/equipos/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable("id") Long id, Model model) {
        Equipo equipo = equipoService.obtenerEquipoPorId(id).orElseThrow(() ->
                new IllegalArgumentException("El equipo con ID " + id + " no existe."));
        model.addAttribute("equipo", equipo);
        return "editar_equipo"; // Vista para editar un equipo
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


 