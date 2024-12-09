package Tennis_ERP.TennisErp.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import Tennis_ERP.TennisErp.DAO.UsuarioDAO;
import Tennis_ERP.TennisErp.domain.usuario;

@Service
public class UsuarioService {

    private final UsuarioDAO usuarioDao;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UsuarioService(UsuarioDAO usuarioDao, PasswordEncoder passwordEncoder) {
        this.usuarioDao = usuarioDao;
        this.passwordEncoder = passwordEncoder;
    }

    public void crearUsuario(usuario nuevoUsuario) {
        nuevoUsuario.setContraseña(passwordEncoder.encode(nuevoUsuario.getContraseña()));
        usuarioDao.save(nuevoUsuario);
    }

    // Método para verificar la contraseña ingresada con la almacenada en la base de datos
    public boolean verificarContraseña(String contrasenaIngresada, String contrasenaCifrada) {
        return passwordEncoder.matches(contrasenaIngresada, contrasenaCifrada);
    }
}