package Tennis_ERP.TennisErp.Service;

import Tennis_ERP.TennisErp.Domain.Equipo;

import java.util.List;
import java.util.Optional;

public interface EquipoService {

    // Crear un nuevo equipo
    Equipo crearEquipo(Equipo equipo);

    // Obtener un equipo por su ID
    Optional<Equipo> obtenerEquipoPorId(Long id);

    // Obtener todos los equipos
    List<Equipo> listarEquipos();

    // Actualizar un equipo existente
    Equipo actualizarEquipo(Equipo equipo);

    // Eliminar un equipo por su ID
    void eliminarEquipo(Long id);

    // Listar equipos por nombre de categoría
    List<Equipo> findEquiposByNombreCategoria(String nombreCategoria);
}
