package Tennis_ERP.TennisErp.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import Tennis_ERP.TennisErp.domain.Usuario;
import Tennis_ERP.TennisErp.DAO.UsuarioDAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioDAO usuarioDAO;

    @Override
    @Transactional
    public Usuario saveUsuario(Usuario usuario) {
        return usuarioDAO.save(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> getAllUsuarios() {
        return usuarioDAO.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> getUsuarioById(Long id) {
        return usuarioDAO.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> findByNombreUsuario(String nombreUsuario) {
        return Optional.ofNullable(usuarioDAO.findByNombreUsuario(nombreUsuario));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> findByEmail(String email) {
        return Optional.ofNullable(usuarioDAO.findByEmail(email));
    }

    @Override
    @Transactional
    public void deleteUsuario(Long id) {
        usuarioDAO.deleteById(id);
    }
}