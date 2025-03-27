package Tennis_ERP.TennisErp.service;

import Tennis_ERP.TennisErp.domain.Rol;
import java.util.List;
import java.util.Optional;

public interface RolService {

    Rol saveRol(Rol rol);

    List<Rol> getAllRoles();

    Optional<Rol> getRolById(Long id);

    Optional<Rol> findByNombreRol(String nombreRol);

    void deleteRol(Long id);
}