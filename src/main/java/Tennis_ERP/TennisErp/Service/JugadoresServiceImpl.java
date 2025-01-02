package Tennis_ERP.TennisErp.service;

import Tennis_ERP.TennisErp.DAO.JugadorDAO;
import Tennis_ERP.TennisErp.domain.Jugadores;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class JugadoresServiceImpl implements JugadoresService {

    @Autowired
    private JugadorDAO jugadoresDAO;

    @Override
    public Jugadores saveJugador(Jugadores jugador) {
        return jugadoresDAO.save(jugador);
    }

    @Override
    public Optional<Jugadores> findJugadorById(Long id) {  // Cambiado a Long para mantener consistencia
        return jugadoresDAO.findById(id);
    }

    @Override
    public List<Jugadores> findAllJugadores() {
        return jugadoresDAO.findAll();
    }

    @Override
    public Jugadores updateJugador(Long id, Jugadores jugador) {  // Cambiado a Long para mantener consistencia
        Optional<Jugadores> existingJugador = jugadoresDAO.findById(id);
        if (existingJugador.isPresent()) {
            Jugadores updatedJugador = existingJugador.get();
            updatedJugador.setNombre(jugador.getNombre());
            updatedJugador.setApellidos(jugador.getApellidos());
            updatedJugador.setRol(jugador.getRol());
            return jugadoresDAO.save(updatedJugador);
        } else {
            throw new EntityNotFoundException("Jugador no encontrado con ID: " + id);  // Excepción más específica
        }
    }

    @Override
    public void deleteJugador(Long id) {  // Cambiado a Long para mantener consistencia
        if (jugadoresDAO.existsById(id)) {
            jugadoresDAO.deleteById(id);
        } else {
            throw new EntityNotFoundException("Jugador no encontrado con ID: " + id);  // Excepción más específica
        }
    }
}
