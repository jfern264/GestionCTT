package Tennis_ERP.TennisErp.Service;

import Tennis_ERP.TennisErp.DAO.EquipoDAO;
import Tennis_ERP.TennisErp.domain.Equipo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EquipoServiceImpl implements EquipoService {

    private EquipoDAO equipoDAO;

    @Autowired
    public void setEquipoDAO(EquipoDAO equipoDAO) {
        this.equipoDAO = equipoDAO;
    }

    @Override
    public Equipo crearEquipo(Equipo equipo) {
        // Guardar el equipo en la base de datos
        return equipoDAO.save(equipo);
    }

    @Override
    public Optional<Equipo> obtenerEquipoPorId(Long id) {
        return equipoDAO.findById(id);
    }

    @Override
    public List<Equipo> listarEquipos() {
        // Obtener todos los equipos
        return equipoDAO.findAll();
    }

    @Override
    public Equipo actualizarEquipo(Equipo equipo) {
        // Verifica que el equipo existe antes de actualizarlo
            return equipoDAO.save(equipo);
    }


    @Override
    public void eliminarEquipo(Long id) {
        // Eliminar el equipo por su ID
        equipoDAO.deleteById(id);
    }

    // Métodos adicionales usando las consultas personalizadas del DAO
    public List<Equipo> buscarEquiposPorLiga(String liga) {
        return equipoDAO.findByLiga(liga);
    }

    public List<Equipo> buscarEquiposPorCategoria(String categoria) {
        return equipoDAO.findByCategoria(categoria);
    }

    public List<Equipo> buscarEquiposPorPuntos(int puntos) {
        return equipoDAO.findByPuntos(puntos);
    }

    public List<Equipo> buscarEquiposPorRangoDePuntos(int minPuntos, int maxPuntos) {
        return equipoDAO.findByPuntosBetween(minPuntos, maxPuntos);
    }

    public List<Equipo> buscarEquiposPorPrecioMayorQue(double precioMinimo) {
        return equipoDAO.findEquiposConPrecioMayorQue(precioMinimo);
    }
}
