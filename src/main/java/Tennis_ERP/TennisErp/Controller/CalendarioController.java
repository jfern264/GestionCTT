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

    // Lista para almacenar eventos (reemplazar con una base de datos en producción)
    private List<Event> events = new ArrayList<>();

    @GetMapping("/calendario")
    public String mostrarCalendario(Model model) {
        // Obtener la fecha actual
        LocalDate currentDate = LocalDate.now();

        // Crear el calendario del mes
        List<List<Integer>> calendario = new ArrayList<>();
        LocalDate firstDayOfMonth = currentDate.withDayOfMonth(1);
        int firstDayOfWeek = firstDayOfMonth.getDayOfWeek().getValue(); // 1 = Monday, 7 = Sunday
        int lastDayOfMonth = currentDate.lengthOfMonth();

        // Rellenar el calendario con los días
        List<Integer> week = new ArrayList<>();
        for (int i = 1; i < firstDayOfWeek; i++) {
            week.add(0); // Espacios vacíos hasta el primer día del mes
        }

        for (int i = 1; i <= lastDayOfMonth; i++) {
            week.add(i);
            if (week.size() == 7) {
                calendario.add(new ArrayList<>(week));
                week.clear();
            }
        }

        if (!week.isEmpty()) {
            calendario.add(new ArrayList<>(week)); // Añadir la última semana si es incompleta
        }

        // Pasar los datos al modelo
        model.addAttribute("currentYear", currentDate.getYear());
        model.addAttribute("currentMonth", currentDate.getMonthValue());
        model.addAttribute("events", events);
        model.addAttribute("calendario", calendario);

        return "calendario"; // Nombre de la vista HTML
    }

    @PostMapping("/calendario")
    public String agregarEvento(@RequestParam("fecha") String fecha, @RequestParam("hora") String hora, @RequestParam("titulo") String titulo, @RequestParam("descripcion") String descripcion) {
        // Crear el nuevo evento
        Event nuevoEvento = new Event(fecha, hora, titulo, descripcion);
        events.add(nuevoEvento);

        // Redirigir para refrescar la página con el nuevo evento
        return "redirect:/calendario"; // Redirige a la página de calendario
    }
}
