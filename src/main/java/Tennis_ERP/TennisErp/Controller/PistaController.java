package Tennis_ERP.TennisErp.Controller;

import Tennis_ERP.TennisErp.domain.Pista;
import Tennis_ERP.TennisErp.service.PistaService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PistaController {

    @Autowired
    private PistaService pistaService;

    @GetMapping("/pistas")
    public String verPistas(Model model) {
        List<Pista> pistas = pistaService.obtenerTodasPistas();
        model.addAttribute("pistas", pistas); // Agrega las pistas al modelo
        return "Pistas"; // El nombre de la vista (archivo HTML)
    }

    @GetMapping("/adminpistas")
    public String administrarPistas(Model model) {
        List<Pista> pistas = pistaService.obtenerTodasPistas();
        model.addAttribute("pistas", pistas); // Agrega las pistas al modelo
        return "AdminPistas"; // El nombre de la vista (archivo HTML)
    }

    // Mostrar formulario para añadir una nueva pista
    @GetMapping("/addpista")
    public String mostrarFormularioNuevaPista(Model model) {
        model.addAttribute("pista", new Pista()); // Pasa un objeto vacío para el formulario
        return "AddPistas"; // El nombre del archivo HTML para el formulario
    }

    // Procesar los datos del formulario y añadir la pista
    @PostMapping("/addPista")
    public String procesarNuevaPista(@ModelAttribute("pista") Pista pista) {
        pistaService.crearPista(pista); // Llama al servicio para guardar la pista
        return "redirect:/adminpistas"; // Redirige a la lista de pistas después de guardar
    }
}
