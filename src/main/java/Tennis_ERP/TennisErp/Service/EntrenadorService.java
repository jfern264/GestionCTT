package Tennis_ERP.TennisErp.service;

import Tennis_ERP.TennisErp.domain.Entrenador;

import java.util.List;
import java.util.Optional;

public interface EntrenadorService {
    List<Entrenador> listarEntrenadores();
    Entrenador crearEntrenador(Entrenador entrenador);
    Optional<Entrenador> obtenerEntrenadorPorId(Long id);
    void eliminarEntrenador(Long id);
    Entrenador actualizarEntrenador(Entrenador entrenador);

}
