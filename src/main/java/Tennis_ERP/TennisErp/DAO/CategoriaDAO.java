package Tennis_ERP.TennisErp.DAO;

import Tennis_ERP.TennisErp.Domain.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CategoriaDAO extends JpaRepository<Categoria, Long> {

    List<Categoria> findByLiga_Id(Long ligaId);
}
