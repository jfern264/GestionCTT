package Tennis_ERP.TennisErp.DAO;

import Tennis_ERP.TennisErp.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface UsuarioDAO extends JpaRepository<Usuario, Long> {
    
    Usuario findByNombreUsuario(String nombreUsuario);

    Usuario findByEmail(String email);

    boolean existsByNombreUsuario(String nombreUsuario);

    boolean existsByEmail(String email);

    List<Usuario> findByRoles_Id(Long rolId);
}