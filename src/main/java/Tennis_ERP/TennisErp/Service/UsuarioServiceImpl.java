package Tennis_ERP.TennisErp.Service;

import Tennis_ERP.TennisErp.DAO.CategoriaDAO;
import Tennis_ERP.TennisErp.DAO.RolDAO;
import Tennis_ERP.TennisErp.DAO.UsuarioCategoriaDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import Tennis_ERP.TennisErp.domain.Usuario;
import Tennis_ERP.TennisErp.DAO.UsuarioDAO;
import Tennis_ERP.TennisErp.domain.Categoria;
import Tennis_ERP.TennisErp.domain.Rol;
import Tennis_ERP.TennisErp.domain.UsuarioCategoria;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioDAO usuarioDAO;

    @Autowired
    private RolDAO rolDAO;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CategoriaDAO categoriaDAO;

    @Autowired
    private UsuarioCategoriaDAO usuarioCategoriaDAO;

    @Override
    @Transactional
    public Usuario saveUsuario(Usuario user) {
        if (user.getPassword() != null && !user.getPassword().startsWith("$2a$")) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return usuarioDAO.save(user);
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

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> getUsuariosPorRol(String nombreRol) {
        Rol rol = rolDAO.findByNombreRol(nombreRol);
        return usuarioDAO.findByRoles(rol);
    }

    @Override
    public Optional<Usuario> findByDni(String dni) {
        return usuarioDAO.findByDni(dni);
    }

    public String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> getJugadoresDisponibles(Long categoriaId) {
        // 1. Obtener el rol “Jugador”
        Rol rolJugador = rolDAO.findByNombreRol("Jugador");

        // 2. Todos los usuarios con ese rol
        List<Usuario> todosJugadores = usuarioDAO.findByRoles_Id(rolJugador.getId());

        // 3. Filtrar aquellos que ya estén en la categoría
        List<Long> idsYaAsignados = usuarioCategoriaDAO
                .findByCategoria_Id(categoriaId)
                .stream()
                .map(UsuarioCategoria::getUsuario)
                .map(Usuario::getId)
                .collect(Collectors.toList());

        // 4. Quedarse solo con los que NO están en idsYaAsignados
        return todosJugadores.stream()
                .filter(u -> !idsYaAsignados.contains(u.getId()))
                .collect(Collectors.toList());
    }
}
