package Tennis_ERP.TennisErp.DAO;

import Tennis_ERP.TennisErp.domain.usuario;  // Importando la clase correctamente
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioDAO extends JpaRepository<usuario, Integer> {

    Optional<usuario> findByNombre(String nombre);  // Método para buscar por nombre
}
