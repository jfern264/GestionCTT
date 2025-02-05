package Tennis_ERP.TennisErp.DAO;

import Tennis_ERP.TennisErp.domain.Entrenador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntrenadorDAO extends JpaRepository<Entrenador, Long> {

    public Entrenador save(Entrenador entrenador);

    public void deleteById(Long id);
}
