package Tennis_ERP.TennisErp.Controller;

import Tennis_ERP.TennisErp.Service.EquipoService;
import Tennis_ERP.TennisErp.domain.Equipo;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class EquipoController {

    @Autowired
    private EquipoService equipoService;

    // Endpoint para mostrar todos los equipos
    @GetMapping("/equipos")
    public String mostrarEquipos(Model model) {
        List<Equipo> equipos = equipoService.listarEquipos();
        model.addAttribute("equipos", equipos);
        return "equipo";
    }
    
    @GetMapping("/equipo_categoria/{categoria}")
    public String mostrarCategoria(@PathVariable String categoria, Model model) {
        List<Equipo> equipos = equipoService.listarEquipos();
        model.addAttribute("equipos", equipos);
        return "equipo_categoria/" + categoria;
    }
}
