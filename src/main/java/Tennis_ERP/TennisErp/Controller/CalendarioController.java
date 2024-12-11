package Tennis_ERP.TennisErp.Controller;

import Tennis_ERP.TennisErp.domain.Event;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
public class CalendarioController {

    // List to store events (for simplicity, replace with a database in production)
    private List<Event> events = new ArrayList<>();

    @GetMapping("/calendario")
    public String mostrarCalendario(Model model) {
        // Obtener la fecha actual
        LocalDate currentDate = LocalDate.now();

        // Pasar el año, mes y eventos al modelo
        model.addAttribute("currentYear", currentDate.getYear());
        model.addAttribute("currentMonth", currentDate.getMonthValue());
        model.addAttribute("events", events);

        return "calendario"; // Nombre de la vista HTML
    }

    @PostMapping("/calendario")
    public String agregarEvento(@RequestParam("fecha") String fecha, @RequestParam("descripcion") String descripcion) {
        // Crear el nuevo evento
        Event nuevoEvento = new Event(fecha, descripcion);
        events.add(nuevoEvento);

        // Guardar el nuevo estado de eventos (puedes hacerlo en una base de datos en lugar de memoria)
        // Aquí no se hace persistencia, solo muestra cómo se agrega el evento.
        return "redirect:/calendario"; // Redirige para refrescar la página con el nuevo evento
    }
}
