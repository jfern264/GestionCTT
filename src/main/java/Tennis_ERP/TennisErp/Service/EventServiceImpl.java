package Tennis_ERP.TennisErp.Service;

import Tennis_ERP.TennisErp.DAO.EventDAO;
import Tennis_ERP.TennisErp.domain.Event;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventDAO eventDAO;

    @Override
    public List<Event> obtenerEventos() {
        return eventDAO.findAll();
    }

    @Override
    public void agregarEvento(Event evento) {
        eventDAO.save(evento);
    }

    @Override
    public Event obtenerEventoPorFecha(String fecha) {
        LocalDate localDate = LocalDate.parse(fecha);  // Convierte el String a LocalDate
        Optional<Event> evento = eventDAO.findByDate(localDate); // Pasa LocalDate, no String
        return evento.orElse(null);
    }

    @Override
    public void eliminarEvento(int id) {
        eventDAO.deleteById(Long.MIN_VALUE); // Aquí el id está declarado como int
    }

    @Override
    public List<List<Integer>> crearCalendario(LocalDate currentDate) {
        List<List<Integer>> calendario = new ArrayList<>();
        LocalDate firstDayOfMonth = currentDate.withDayOfMonth(1);
        int firstDayOfWeek = firstDayOfMonth.getDayOfWeek().getValue(); // 1 = Monday, 7 = Sunday
        int lastDayOfMonth = currentDate.lengthOfMonth();

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

        return calendario;
    }
}
