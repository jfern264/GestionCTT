package Tennis_ERP.TennisErp.Service;

import Tennis_ERP.TennisErp.Domain.Event;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public interface EventService {

    List<Event> obtenerEventos();

    void agregarEvento(Event evento);

    Event obtenerEventoPorFecha(String fecha);

    void eliminarEvento(int id);

    // Método para crear el calendario para un mes específico
    List<List<Integer>> crearCalendario(LocalDate currentDate);
}
