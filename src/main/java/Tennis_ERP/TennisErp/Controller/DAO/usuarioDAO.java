package Tennis_ERP.TennisErp.Controller.DAO;

import Tennis_ERP.TennisErp.Domain.usuario;  // Importando la clase correctamente
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface usuarioDAO extends JpaRepository<usuario, Integer> {

    Optional<usuario> findByNombre(String nombre);  // Método para buscar por nombre
}