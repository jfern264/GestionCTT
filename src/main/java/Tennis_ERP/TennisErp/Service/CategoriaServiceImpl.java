package Tennis_ERP.TennisErp.service;


import Tennis_ERP.TennisErp.DAO.CategoriaDAO;
import Tennis_ERP.TennisErp.domain.Categoria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class CategoriaServiceImpl implements CategoriaService {

    @Autowired
    private CategoriaDAO categoriaDAO;

    @Override
    @Transactional
    public Categoria saveCategoria(Categoria categoria) {
        return categoriaDAO.save(categoria);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Categoria> getAllCategorias() {
        return categoriaDAO.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Categoria> getCategoriaById(Long id) {
        return categoriaDAO.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Categoria> findByLigaId(Long ligaId) {
        return categoriaDAO.findByLiga_Id(ligaId);
    }

    @Override
    @Transactional
    public void deleteCategoria(Long id) {
        categoriaDAO.deleteById(id);
    }
}
