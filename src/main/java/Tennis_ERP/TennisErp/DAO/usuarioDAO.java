package Tennis_ERP.TennisErp.DAO;

import Tennis_ERP.TennisErp.domain.usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface usuarioDAO extends JpaRepository<usuario, Integer> {

    /**
     * Busca un usuario por su nombre.
     *
     * @param nombre El nombre del usuario.
     * @return Un Optional que contiene el usuario si se encuentra, o vacío en
     * caso contrario.
     */
    Optional<usuario> findByNombre(String nombre);
}