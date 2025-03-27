package Tennis_ERP.TennisErp.Service;

import Tennis_ERP.TennisErp.domain.Liga;
import java.util.List;
import java.util.Optional;

public interface LigaService {

    Liga saveLiga(Liga liga);

    List<Liga> getAllLigas();

    Optional<Liga> getLigaById(Long id);

    Optional<Liga> findByNombre(String nombre);

    void deleteLiga(Long id);
}
