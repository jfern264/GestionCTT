package Tennis_ERP.TennisErp.DAO;

import Tennis_ERP.TennisErp.domain.Liga;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LigaDAO extends JpaRepository<Liga, Long> {

    Liga findByNombre(String nombre);
}
