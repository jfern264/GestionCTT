package Tennis_ERP.TennisErp.DAO;

import Tennis_ERP.TennisErp.domain.Liga;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LigaDAO extends JpaRepository<Liga, Long> {

    Liga findByNombre(String nombre);
}
