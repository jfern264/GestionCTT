package Tennis_ERP.TennisErp.controller;

import Tennis_ERP.TennisErp.domain.Pista;
import Tennis_ERP.TennisErp.service.PistaService;
import Tennis_ERP.TennisErp.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

@Controller
public class PistaController {

    @Autowired private PistaService pistaService;
    @Autowired private EventService eventService;

    @GetMapping("/adminpistas")
    public String adminPistas(Model model) {
        model.addAttribute("pistas", pistaService.obtenerTodasPistas());
        return "pistas/gestion_pistas/adminpistas"; 
    }

    @GetMapping("/pistas")
    public String verPistas(Model model) {
        model.addAttribute("pistas", pistaService.obtenerTodasPistas());
        return "pistas/gestion_pistas/pistas";
    }

    @GetMapping("/calendario")
    public String verCalendario(Model model) {
        LocalDate hoy = LocalDate.now();
        model.addAttribute("events", eventService.obtenerEventos());
        model.addAttribute("calendario", eventService.crearCalendario(hoy));
        return "extras/calendario"; 
    }
}