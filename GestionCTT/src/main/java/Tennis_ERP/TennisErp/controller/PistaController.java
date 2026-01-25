package Tennis_ERP.TennisErp.controller;

import Tennis_ERP.TennisErp.domain.Event;
import Tennis_ERP.TennisErp.domain.Pista;
import Tennis_ERP.TennisErp.service.PistaService;
import Tennis_ERP.TennisErp.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.util.List;

@Controller
public class PistaController {

    @Autowired private PistaService pistaService;
    @Autowired private EventService eventService;

    @GetMapping("/adminpistas")
    public String adminPistas(Model model) {
        model.addAttribute("pistas", pistaService.obtenerTodasPistas());
        return "pistas/gestion_pistas/adminpistas"; 
    }

    @GetMapping("/adminpistas/addpista")
    public String formularioNuevaPista(Model model) {
        model.addAttribute("pista", new Pista());
        return "pistas/gestion_pistas/addPistas";
    }

    @PostMapping("addPista")
    public String guardarNuevaPista(@ModelAttribute("pista") Pista pista) {
        pistaService.crearPista(pista);
        return "redirect:/adminpistas";
    }

    @GetMapping("/pistas")
    public String verPistas(Model model) {
        model.addAttribute("pistas", pistaService.obtenerTodasPistas());
        return "pistas/gestion_pistas/pistas";
    }

    @GetMapping("/calendario")
    public String verCalendario(Model model) {
        model.addAttribute("eventosJson", eventService.obtenerEventosComoJson());
        return "extras/calendario";
    }


    @GetMapping("/menu_admin/calendario")
    public String verCalendarioAdmin(Model model) {
            // 1. Datos para el calendario (JSON)
            model.addAttribute("eventosJson", eventService.obtenerEventosComoJson());
            
            // 2. Datos para el formulario de creación
            model.addAttribute("nuevoEvento", new Event());
            
            // 3. Lista de pistas (Corregido para usar nombrePista)
            model.addAttribute("pistas", pistaService.obtenerTodasPistas());
            
            return "extras/admincalendario";
    }

    @PostMapping("/menu_admin/calendario/guardar")
    public String guardarEvento(@ModelAttribute("nuevoEvento") Event evento) {
        eventService.agregarEvento(evento);
        return "redirect:/menu_admin/calendario";
    }
}