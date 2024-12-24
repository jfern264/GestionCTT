package Tennis_ERP.TennisErp.DAO;

import Tennis_ERP.TennisErp.domain.Equipo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EquipoDAO extends JpaRepository<Equipo, Integer> {

    // Buscar equipos por liga
    List<Equipo> findByLiga(String liga);

    // Buscar equipos por categoría
    List<Equipo> findByCategoria(String categoria);

    // Buscar equipos por nombre exacto del equipo
    Equipo findByEquipo(String equipo);

    // Buscar equipos que contengan una palabra clave en su nombre (ignorando mayúsculas)
    List<Equipo> findByEquipoContainingIgnoreCase(String keyword);

    // Buscar equipos por puntos
    List<Equipo> findByPuntos(int puntos);

    // Buscar equipos por un rango de puntos
    List<Equipo> findByPuntosBetween(int minPuntos, int maxPuntos);

    // Consultas personalizadas usando JPQL
    @Query("SELECT e FROM Equipo e WHERE e.precioLiga > :precioMinimo")
    List<Equipo> findEquiposConPrecioMayorQue(@Param("precioMinimo") double precioMinimo);

    // Consultas personalizadas usando JPQL con LIKE para obtener equipos por parte del nombre
    @Query("SELECT e FROM Equipo e WHERE e.equipo LIKE %:nombre%")
    List<Equipo> findEquiposByPartialName(@Param("nombre") String nombre);
}
