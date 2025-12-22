package Tennis_ERP.TennisErp.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import Tennis_ERP.TennisErp.domain.Rol;

public interface RolDAO extends JpaRepository<Rol, Long> {

    Rol findByNombreRol(String nombreRol);
}
