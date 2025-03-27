package Tennis_ERP.TennisErp.Service;


import Tennis_ERP.TennisErp.DAO.LigaDAO;
import Tennis_ERP.TennisErp.domain.Liga;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class LigaServiceImpl implements LigaService {

    @Autowired
    private LigaDAO ligaDAO;

    @Override
    @Transactional
    public Liga saveLiga(Liga liga) {
        return ligaDAO.save(liga);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Liga> getAllLigas() {
        return ligaDAO.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Liga> getLigaById(Long id) {
        return ligaDAO.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Liga> findByNombre(String nombre) {
        return Optional.ofNullable(ligaDAO.findByNombre(nombre));
    }

    @Override
    @Transactional
    public void deleteLiga(Long id) {
        ligaDAO.deleteById(id);
    }
}