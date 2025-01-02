package Tennis_ERP.TennisErp.DAO;

import Tennis_ERP.TennisErp.domain.rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface rolDAO extends JpaRepository<rol, Integer> {

    // Si necesitas otros métodos personalizados, puedes agregarlos aquí
    rol findByRol(rol.RoleType rol);
}
