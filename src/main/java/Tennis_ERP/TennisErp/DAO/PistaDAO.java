package Tennis_ERP.TennisErp.DAO;

import Tennis_ERP.TennisErp.domain.Pista;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PistaDAO extends JpaRepository<Pista, Long> {

}
