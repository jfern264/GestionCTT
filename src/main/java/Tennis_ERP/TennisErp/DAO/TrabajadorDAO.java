package Tennis_ERP.TennisErp.DAO;

import Tennis_ERP.TennisErp.domain.Trabajador;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrabajadorDAO extends JpaRepository<Trabajador, Long> {

    List<Trabajador> findByNombre(String nombre);

    // Método para buscar por DNI
    Trabajador findByDni(String dni);

    // Método para buscar por correo
    List<Trabajador> findByCorreo(String correo);

    // Método para buscar por nombre y apellidos
    List<Trabajador> findByNombreAndApellidos(String nombre, String apellidos);

    List<Trabajador> findByRolId(Long rolId);
}
