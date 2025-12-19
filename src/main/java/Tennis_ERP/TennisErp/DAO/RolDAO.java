package Tennis_ERP.TennisErp.DAO;

import Tennis_ERP.TennisErp.Domain.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolDAO extends JpaRepository<Rol, Long> {

    Rol findByNombreRol(String nombreRol);
}
