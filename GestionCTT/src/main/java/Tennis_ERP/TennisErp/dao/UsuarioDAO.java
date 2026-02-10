package Tennis_ERP.TennisErp.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import Tennis_ERP.TennisErp.domain.Rol;
import Tennis_ERP.TennisErp.domain.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioDAO extends JpaRepository<Usuario, Long> {

    Usuario findByNombreUsuario(String nombreUsuario);

    Usuario findByEmail(String email);

    boolean existsByNombreUsuario(String nombreUsuario);

    boolean existsByEmail(String email);

    List<Usuario> findByRoles_Id(Long rolId);

    List<Usuario> findByRoles(Rol rol);

    Optional<Usuario> findByDni(String dni);

    @Query("SELECT u FROM Usuario u JOIN u.roles r WHERE r.nombreRol = 'ROLE_JUGADOR' AND u.id NOT IN :idsOcupados")
    List<Usuario> findJugadoresDisponibles(@Param("idsOcupados") List<Long> idsOcupados);

}
