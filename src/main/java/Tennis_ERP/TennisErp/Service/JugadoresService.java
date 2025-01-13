package Tennis_ERP.TennisErp.service;

import Tennis_ERP.TennisErp.domain.Jugadores;
import java.util.List;
import java.util.Optional;

public interface JugadoresService {

    // Guardar un jugador
    Jugadores saveJugador(Jugadores jugador);

    // Buscar un jugador por ID
    Optional<Jugadores> findJugadorById(Long id);  // Usamos Long para mantener la coherencia con el tipo del ID

    // Obtener todos los jugadores
    List<Jugadores> findAllJugadores();

    // Actualizar un jugador existente
    Jugadores updateJugador(Long id, Jugadores jugador);  // Usamos Long también aquí para el ID

    // Eliminar un jugador por ID
    void deleteJugador(Long id);  // Usamos Long en vez de int
}
