package Tennis_ERP.TennisErp.service;

import Tennis_ERP.TennisErp.DAO.CategoriaDAO;
import Tennis_ERP.TennisErp.Domain.Categoria;
import Tennis_ERP.TennisErp.Domain.Liga;
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

    @Override
    @Transactional(readOnly = true)
    public List<Categoria> getCategoriasByLiga(Liga liga) {
        return categoriaDAO.findByLiga_Id(liga.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Categoria> findAllWithUsuariosOrdenados() {
        List<Categoria> categorias = categoriaDAO.findAll();

        for (Categoria categoria : categorias) {
            categoria.getUsuarioCategorias().sort((uc1, uc2) -> {
                if (uc1.isActivo() == uc2.isActivo()) {
                    return Boolean.compare(uc1.isSuplente(), uc2.isSuplente());
                }
                return Boolean.compare(!uc1.isActivo(), !uc2.isActivo()); // Activos primero
            });
        }

        return categorias;
    }
}
