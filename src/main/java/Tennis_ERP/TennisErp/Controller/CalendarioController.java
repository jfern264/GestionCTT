package Tennis_ERP.TennisErp.Controller;

import Tennis_ERP.TennisErp.Service.EventService; // Importamos el servicio
import Tennis_ERP.TennisErp.domain.Event;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Controller
public class CalendarioController {

    private final EventService eventService; // Inyección de dependencias de EventService

    // Constructor que inyecta el servicio
    public CalendarioController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/calendario")
    public String mostrarCalendario(Model model) {
        // Obtener la fecha actual
        LocalDate currentDate = LocalDate.now();

        // Crear el calendario del mes
        List<List<Integer>> calendario = eventService.crearCalendario(currentDate);

        // Obtener todos los eventos del servicio
        List<Event> events = eventService.obtenerEventos();

        // Pasar los datos al modelo
        model.addAttribute("currentYear", currentDate.getYear());
        model.addAttribute("currentMonth", currentDate.getMonthValue());
        model.addAttribute("events", events);
        model.addAttribute("calendario", calendario);

        return "calendario"; // Nombre de la vista HTML
    }

    @PostMapping("/calendario")
    public String agregarEvento(@RequestParam("fecha") String fecha,
            @RequestParam("hora") String hora,
            @RequestParam("titulo") String titulo,
            @RequestParam("descripcion") String descripcion) {
        // Convertir las cadenas de fecha y hora a LocalDate y LocalTime
        LocalDate localDate = LocalDate.parse(fecha);  // Convierte la cadena de fecha a LocalDate
        LocalTime localTime = LocalTime.parse(hora);   // Convierte la cadena de hora a LocalTime

        // Crear el nuevo evento
        Event nuevoEvento = new Event(localDate, localTime, titulo, descripcion);  // Usar el constructor para crear el evento

        // Agregar el evento usando el servicio
        eventService.agregarEvento(nuevoEvento);  // Llamamos al servicio para agregar el evento

        // Redirigir para refrescar la página con el nuevo evento
        return "redirect:/calendario"; // Redirige a la página de calendario
    }

}
