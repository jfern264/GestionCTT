package Tennis_ERP.TennisErp.Controller;

import Tennis_ERP.TennisErp.Domain.Pista;
import Tennis_ERP.TennisErp.service.PistaService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PistaController {

    @Autowired
    private PistaService pistaService;

    @GetMapping("/pistas")
    public String verPistas(Model model) {
        List<Pista> pistas = pistaService.obtenerTodasPistas();
        model.addAttribute("pistas", pistas);
        return "Pistas";
    }

    @GetMapping("/adminpistas")
    public String administrarPistas(Model model) {
        List<Pista> pistas = pistaService.obtenerTodasPistas();
        model.addAttribute("pistas", pistas);
        return "AdminPistas";
    }

    @GetMapping("/adminpistas/addpista")
    public String mostrarFormularioNuevaPista(Model model) {
        model.addAttribute("pista", new Pista());
        return "AddPistas";
    }

    @PostMapping("/adminpistas/addPista")
    public String procesarNuevaPista(@ModelAttribute("pista") Pista pista) {
        pistaService.crearPista(pista);
        return "redirect:/adminpistas";
    }

    @GetMapping("/adminpistas/editpista/{id}")
    public String mostrarFormularioEditarPista(@PathVariable("id") Long id, Model model) {
        Pista pista = pistaService.obtenerPistaPorId(id)
                .orElseThrow(() -> new RuntimeException("Pista no encontrada con ID: " + id));
        model.addAttribute("pista", pista);
        return "editar_Pista";
    }

    @PostMapping("/adminpistas/editpista/{id}")
    public String procesarEditarPista(@PathVariable("id") Long id, @ModelAttribute("pista") Pista pistaActualizada) {
        pistaService.actualizarPista(id, pistaActualizada);
        return "redirect:/adminpistas";
    }

    @GetMapping("/adminpistas/deletepista/{id}")
    public String eliminarPista(@PathVariable("id") Long id) {
        pistaService.eliminarPista(id);
        return "redirect:/adminpistas";
    }

}
