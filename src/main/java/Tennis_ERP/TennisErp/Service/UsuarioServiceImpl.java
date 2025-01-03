package Tennis_ERP.TennisErp.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import Tennis_ERP.TennisErp.DAO.usuarioDAO;
import Tennis_ERP.TennisErp.domain.usuario;

@Service
@Transactional
public class UsuarioServiceImpl implements UsuarioService {

    private final usuarioDAO usuarioDao;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UsuarioServiceImpl(usuarioDAO usuarioDao, PasswordEncoder passwordEncoder) {
        this.usuarioDao = usuarioDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void crearUsuario(usuario nuevoUsuario) {
        nuevoUsuario.setContraseña(passwordEncoder.encode(nuevoUsuario.getContraseña()));
        usuarioDao.save(nuevoUsuario);
    }

    @Override
    public usuario obtenerUsuarioPorNombre(String nombre) {
        return usuarioDao.findByNombre(nombre)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + nombre));
    }

    @Override
    public boolean verificarContraseña(String contrasenaIngresada, String contrasenaCifrada) {
        return passwordEncoder.matches(contrasenaIngresada, contrasenaCifrada);
    }

    @Override
    public void actualizarUsuario(usuario usuarioActualizado) {
        if (!usuarioDao.existsById(usuarioActualizado.id)) {
            throw new RuntimeException("Usuario no encontrado con ID: " + usuarioActualizado.id);
        }
        usuarioDao.save(usuarioActualizado);
    }

    @Override
    public void eliminarUsuarioPorId(int id) {
        if (!usuarioDao.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado con ID: " + id);
        }
        usuarioDao.deleteById(id);
    }
}
