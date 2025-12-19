package Tennis_ERP.TennisErp.DAO;

import Tennis_ERP.TennisErp.Domain.Equipo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EquipoDAO extends JpaRepository<Equipo, Long> {

    // Buscar equipos por liga
    List<Equipo> findByLiga(String liga);

    // Buscar equipos por puntos
    List<Equipo> findByPuntos(int puntos);

    @Query("SELECT e FROM Equipo e JOIN e.categoria c WHERE c.nombre = :nombreCategoria")
    List<Equipo> findEquiposByNombreCategoria(@Param("nombreCategoria") String nombreCategoria);


}
