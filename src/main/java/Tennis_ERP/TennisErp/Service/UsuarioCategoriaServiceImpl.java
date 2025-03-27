package Tennis_ERP.TennisErp.Service;

import Tennis_ERP.TennisErp.DAO.UsuarioCategoriaDAO;
import Tennis_ERP.TennisErp.domain.UsuarioCategoria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioCategoriaServiceImpl implements UsuarioCategoriaService {

    @Autowired
    private UsuarioCategoriaDAO usuarioCategoriaDAO;

    @Override
    @Transactional
    public UsuarioCategoria saveUsuarioCategoria(UsuarioCategoria uc) {
        return usuarioCategoriaDAO.save(uc);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioCategoria> getAllUsuarioCategorias() {
        return usuarioCategoriaDAO.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UsuarioCategoria> getById(Long id) {
        return usuarioCategoriaDAO.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioCategoria> findByCategoriaId(Long categoriaId) {
        return usuarioCategoriaDAO.findByCategoria_Id(categoriaId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioCategoria> findByUsuarioId(Long usuarioId) {
        return usuarioCategoriaDAO.findByUsuario_Id(usuarioId);
    }

    @Override
    @Transactional
    public void deleteUsuarioCategoria(Long id) {
        usuarioCategoriaDAO.deleteById(id);
    }
}