package Tennis_ERP.TennisErp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import Tennis_ERP.TennisErp.dao.CategoriaDAO;
import Tennis_ERP.TennisErp.dao.RolDAO;
import Tennis_ERP.TennisErp.dao.UsuarioCategoriaDAO;
import Tennis_ERP.TennisErp.dao.UsuarioDAO;
import Tennis_ERP.TennisErp.domain.Categoria;
import Tennis_ERP.TennisErp.domain.Rol;
import Tennis_ERP.TennisErp.domain.Usuario;
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

    @Autowired
    private RolService rolService;

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
        Usuario usuario = usuarioDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // BLOQUEO DE SEGURIDAD: No borrar al superadmin
        if ("admin".equals(usuario.getNombreUsuario())) {
            throw new RuntimeException(
                    "OPERACIÓN DENEGADA: El usuario 'admin' es vital para el sistema y no puede ser eliminado.");
        }

        // Si no es admin, procedemos con el borrado de inscripciones y luego el usuario
        List<UsuarioCategoria> inscripciones = usuarioCategoriaDAO.findAll().stream()
                .filter(uc -> uc.getUsuario().getId().equals(id))
                .collect(Collectors.toList());

        usuarioCategoriaDAO.deleteAll(inscripciones);
        usuarioDAO.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> getUsuariosPorRol(String nombreRol) {
        Rol rol = rolDAO.findByNombreRol(nombreRol)
                .orElseThrow(() -> new IllegalStateException(
                        "No existe el rol '" + nombreRol + "' en la base de datos"));

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
        Rol rolJugador = rolDAO.findByNombreRol("ROLE_JUGADOR")
                .orElseThrow(() -> new IllegalStateException("No existe el rol 'ROLE_JUGADOR' en la base de datos"));

        // 2. Todos los usuarios con ese rol
        List<Usuario> todosJugadores = usuarioDAO.findByRoles(rolJugador);

        // 3. Filtrar aquellos que ya estén en la categoría
        List<Long> idsYaAsignados = usuarioCategoriaDAO
                .findByCategoria_Id(categoriaId)
                .stream()
                .map(UsuarioCategoria::getUsuario)
                .map(Usuario::getId)
                .toList();

        // 4. Quedarse solo con los que NO están en idsYaAsignados
        return todosJugadores.stream()
                .filter(u -> !idsYaAsignados.contains(u.getId()))
                .toList();
    }

    @Override
    @Transactional
    public void updateUsuario(Long id, Usuario form) {
        Usuario db = usuarioDAO.findById(id).orElseThrow(() -> new RuntimeException("No existe"));

        if ("admin".equals(db.getNombreUsuario())) {
            boolean tieneAdmin = form.getRoles().stream()
                    .anyMatch(r -> r.getNombreRol().equals("ROLE_ADMIN"));

            if (!tieneAdmin) {
                throw new RuntimeException(
                        "PROTECCIÓN DE CUENTA: No puedes quitar el rol ROLE_ADMIN al usuario principal.");
            }
        }
        // Sincronización de todos los campos necesarios
        db.setNombreUsuario(form.getNombreUsuario());
        db.setNombre(form.getNombre());
        db.setPrimerApellido(form.getPrimerApellido());
        db.setSegundoApellido(form.getSegundoApellido());
        db.setDni(form.getDni());
        db.setEmail(form.getEmail());
        db.setTelefono(form.getTelefono());
        db.setRoles(form.getRoles());

        // Lógica de contraseña: Solo si se escribe una nueva
        if (form.getPassword() != null && !form.getPassword().trim().isEmpty()) {
            db.setPassword(form.getPassword());
        }

        usuarioDAO.save(db);
    }
}
