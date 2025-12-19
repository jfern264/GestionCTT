package Tennis_ERP.TennisErp.DAO;

import Tennis_ERP.TennisErp.Domain.UsuarioCategoria;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UsuarioCategoriaDAO extends JpaRepository<UsuarioCategoria, Long> {

    List<UsuarioCategoria> findByCategoria_Id(Long categoriaId);

    List<UsuarioCategoria> findByUsuario_Id(Long usuarioId);
}
