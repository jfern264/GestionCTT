package Tennis_ERP.TennisErp.Controller;

import Tennis_ERP.TennisErp.Service.EventService;
import Tennis_ERP.TennisErp.Domain.Event;
import Tennis_ERP.TennisErp.Domain.Pista; // Importar la clase Pista
import Tennis_ERP.TennisErp.service.PistaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Controller
public class CalendarioController {

    @Autowired
    private EventService eventService; // Inyección automática del servicio de eventos

    @Autowired
    private PistaService pistaService; // Inyección automática del servicio de pistas

    // No es necesario tener el constructor cuando se usa @Autowired
    // El constructor se elimina en este caso
    @GetMapping("/calendario")
    public String mostrarCalendario(Model model) {
        LocalDate currentDate = LocalDate.now();
        List<List<Integer>> calendario = eventService.crearCalendario(currentDate);
        List<Event> events = eventService.obtenerEventos();

        model.addAttribute("currentYear", currentDate.getYear());
        model.addAttribute("currentMonth", currentDate.getMonthValue());
        model.addAttribute("events", events);
        model.addAttribute("calendario", calendario);

        return "calendario"; // Vista HTML para los usuarios
    }

    @GetMapping("/admincalendario")
    public String administrarCalendario(Model model) {
        // Obtener la fecha actual
        LocalDate currentDate = LocalDate.now();

        // Crear el calendario del mes
        List<List<Integer>> calendario = eventService.crearCalendario(currentDate);

        // Obtener todos los eventos del servicio
        List<Event> events = eventService.obtenerEventos();

        // Obtener las pistas disponibles desde el servicio
        List<Pista> pistas = pistaService.obtenerTodasPistas();  // Método para obtener todas las pistas

        // Pasar los datos al modelo
        model.addAttribute("currentYear", currentDate.getYear());
        model.addAttribute("currentMonth", currentDate.getMonthValue());
        model.addAttribute("events", events);
        model.addAttribute("calendario", calendario);
        model.addAttribute("pistas", pistas);  // Pasamos las pistas al modelo

        return "AdminCalendario"; // Nombre de la vista HTML
    }

    @PostMapping("/admincalendario")
    public String agregarEvento(@RequestParam("fecha") String fecha,
            @RequestParam("hora") String hora,
            @RequestParam("titulo") String titulo,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("pistaId") Long pistaId) { // Obtener el ID de la pista seleccionada

        // Convertir las cadenas de fecha y hora a LocalDate y LocalTime
        LocalDate localDate = LocalDate.parse(fecha);
        LocalTime localTime = LocalTime.parse(hora);

        // Obtener la pista seleccionada
        Optional<Pista> pistaSeleccionadaOptional = pistaService.obtenerPistaPorId(pistaId);
        if (pistaSeleccionadaOptional.isPresent()) {
            Pista pistaSeleccionada = pistaSeleccionadaOptional.get();
            Event nuevoEvento = new Event(localDate, localTime, titulo, descripcion, pistaSeleccionada);
            eventService.agregarEvento(nuevoEvento);
        } else {
            throw new RuntimeException("La pista seleccionada no existe");
        }

        // Redirigir para refrescar la página con el nuevo evento
        return "redirect:/admincalendario";
    }
}
