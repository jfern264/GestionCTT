package Tennis_ERP.TennisErp.service;

import Tennis_ERP.TennisErp.domain.Categoria;
import Tennis_ERP.TennisErp.domain.Liga;
import java.util.List;
import java.util.Optional;

public interface CategoriaService {

    Categoria saveCategoria(Categoria categoria);

    List<Categoria> getAllCategorias();

    Optional<Categoria> getCategoriaById(Long id);

    List<Categoria> findByLigaId(Long ligaId);

    void deleteCategoria(Long id);
    
    List<Categoria> getCategoriasByLiga(Liga liga);
    
    List<Categoria> findAllWithUsuariosOrdenados();
}
