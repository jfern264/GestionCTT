package Tennis_ERP.TennisErp.DAO;

import Tennis_ERP.TennisErp.domain.Jugadores;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JugadorDAO extends JpaRepository<Jugadores, Long> {  // Cambié Integer por Long

    // Encuentra jugadores por nombre
    List<Jugadores> findByNombre(String nombre);

    // Encuentra jugadores por apellidos
    List<Jugadores> findByApellidos(String apellidos);

    // Encuentra jugadores por rol
    List<Jugadores> findByRolId(Long rolId);  // Cambié el tipo de rolId a Long para consistencia
}