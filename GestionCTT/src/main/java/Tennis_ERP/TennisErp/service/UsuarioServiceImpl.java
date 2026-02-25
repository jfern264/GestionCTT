package Tennis_ERP.TennisErp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import Tennis_ERP.TennisErp.dao.CategoriaDAO;
import Tennis_ERP.TennisErp.dao.RolDAO;
import Tennis_ERP.TennisErp.dao.UsuarioCategoriaDAO;
import Tennis_ERP.TennisErp.dao.UsuarioDAO;
import Tennis_ERP.TennisErp.domain.Categoria;
import Tennis_ERP.TennisErp.domain.Rol;
import Tennis_ERP.TennisErp.domain.Usuario;
import Tennis_ERP.TennisErp.domain.UsuarioCategoria;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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

        if (user.getRoles() != null && !user.getRoles().isEmpty()) {
            user.setRoles(user.getRoles().stream()
                    .map(rol -> rolDAO.findById(rol.getId())
                            .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + rol.getId())))
                    .collect(Collectors.toSet()));
        }

        return usuarioDAO.save(user);
    }

    @Override
    @Transactional
    public Usuario saveUsuarioWithImage(Usuario user, MultipartFile imageFile) {
        if (user.getPassword() != null && !user.getPassword().startsWith("$2a$")) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        if (user.getRoles() != null && !user.getRoles().isEmpty()) {
            user.setRoles(user.getRoles().stream()
                    .map(rol -> rolDAO.findById(rol.getId())
                            .orElseThrow(() -> new RuntimeException("Rol no encontrado")))
                    .collect(Collectors.toSet()));
        }

        if (imageFile != null && !imageFile.isEmpty()) {
            handleUserImage(user, imageFile, null);
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

        if ("admin".equals(usuario.getNombreUsuario())) {
            throw new RuntimeException(
                    "OPERACIÓN DENEGADA: El usuario 'admin' es vital para el sistema y no puede ser eliminado.");
        }

        List<UsuarioCategoria> inscripciones = usuarioCategoriaDAO.findAll().stream()
                .filter(uc -> uc.getUsuario().getId().equals(id))
                .collect(Collectors.toList());

        usuarioCategoriaDAO.deleteAll(inscripciones);

        if (usuario.getAvatar() != null && !usuario.getAvatar().isEmpty()) {
            String uploadDir = System.getProperty("user.dir") + File.separator + "uploads" + File.separator;
            try {
                Files.deleteIfExists(Paths.get(uploadDir + usuario.getAvatar()));
            } catch (IOException e) {
                System.err.println("Error al eliminar imagen del avatar: " + e.getMessage());
            }
        }

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
        Rol rolJugador = rolDAO.findByNombreRol("ROLE_JUGADOR")
                .orElseThrow(() -> new IllegalStateException("No existe el rol 'ROLE_JUGADOR' en la base de datos"));

        List<Usuario> todosJugadores = usuarioDAO.findByRoles(rolJugador);

        List<Long> idsYaAsignados = usuarioCategoriaDAO
                .findByCategoria_Id(categoriaId)
                .stream()
                .map(UsuarioCategoria::getUsuario)
                .map(Usuario::getId)
                .toList();

        return todosJugadores.stream()
                .filter(u -> !idsYaAsignados.contains(u.getId()))
                .toList();
    }

    // ✅ MÉTODO ACTUALIZADO: Sin el rolId, y sincroniza la lista de roles del form
    @Override
    @Transactional
    public void updateUsuario(Long id, Usuario form) {
        Usuario db = usuarioDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));

        db.setNombreUsuario(form.getNombreUsuario());
        db.setNombre(form.getNombre());
        db.setPrimerApellido(form.getPrimerApellido());
        db.setSegundoApellido(form.getSegundoApellido());
        db.setDni(form.getDni());
        db.setDniFamiliar(form.getDniFamiliar());
        db.setGenero(form.getGenero());
        db.setFechaNacimiento(form.getFechaNacimiento());
        db.setMatricula(form.getMatricula());
        db.setFormaDePago(form.getFormaDePago());
        db.setEmail(form.getEmail());
        db.setTelefono(form.getTelefono());

        // ✅ Sincronización de múltiples roles desde los checkboxes
        if (form.getRoles() != null && !form.getRoles().isEmpty()) {
            Set<Rol> nuevosRoles = form.getRoles().stream()
                    .map(rol -> rolDAO.findById(rol.getId())
                            .orElseThrow(() -> new RuntimeException("Rol no encontrado")))
                    .collect(Collectors.toSet());

            // Protección Admin
            if ("admin".equals(db.getNombreUsuario())
                    && nuevosRoles.stream().noneMatch(r -> r.getNombreRol().equals("ROLE_ADMIN"))) {
                throw new RuntimeException("PROTECCIÓN: El usuario admin principal debe conservar su rol.");
            }

            db.setRoles(nuevosRoles);
        } else if ("admin".equals(db.getNombreUsuario())) {
            throw new RuntimeException("PROTECCIÓN: El usuario admin principal no puede quedarse sin roles.");
        } else {
            db.getRoles().clear();
        }

        if (form.getPassword() != null && !form.getPassword().trim().isEmpty()) {
            db.setPassword(passwordEncoder.encode(form.getPassword()));
        }

        usuarioDAO.save(db);
    }

    // ✅ MÉTODO ACTUALIZADO: Sin el rolId
    @Override
    @Transactional
    public void updateUsuarioWithImage(Long id, Usuario form, MultipartFile imageFile) {
        Usuario db = usuarioDAO.findById(id).orElseThrow(() -> new RuntimeException("No existe"));

        db.setNombreUsuario(form.getNombreUsuario());
        db.setNombre(form.getNombre());
        db.setPrimerApellido(form.getPrimerApellido());
        db.setSegundoApellido(form.getSegundoApellido());
        db.setDni(form.getDni());
        db.setDniFamiliar(form.getDniFamiliar());
        db.setGenero(form.getGenero());
        db.setFechaNacimiento(form.getFechaNacimiento());
        db.setMatricula(form.getMatricula());
        db.setFormaDePago(form.getFormaDePago());
        db.setEmail(form.getEmail());
        db.setTelefono(form.getTelefono());

        // ✅ Sincronización de múltiples roles
        if (form.getRoles() != null && !form.getRoles().isEmpty()) {
            Set<Rol> nuevosRoles = form.getRoles().stream()
                    .map(rol -> rolDAO.findById(rol.getId())
                            .orElseThrow(() -> new RuntimeException("Rol no encontrado")))
                    .collect(Collectors.toSet());

            if ("admin".equals(db.getNombreUsuario())
                    && nuevosRoles.stream().noneMatch(r -> r.getNombreRol().equals("ROLE_ADMIN"))) {
                throw new RuntimeException("PROTECCIÓN: El usuario admin principal debe conservar su rol.");
            }
            db.setRoles(nuevosRoles);
        } else if ("admin".equals(db.getNombreUsuario())) {
            throw new RuntimeException("PROTECCIÓN: El usuario admin principal no puede quedarse sin roles.");
        } else {
            db.getRoles().clear();
        }

        if (form.getPassword() != null && !form.getPassword().trim().isEmpty()) {
            db.setPassword(passwordEncoder.encode(form.getPassword()));
        }

        handleUserImage(db, imageFile, db);

        usuarioDAO.save(db);
    }

    private void handleUserImage(Usuario user, MultipartFile imageFile, Usuario existingUser) {
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                // ✅ CAMBIO A LA RUTA EXTERNA PREPARADA PARA PRODUCCIÓN
                String rootPath = System.getProperty("user.dir");
                String uploadDir = rootPath + File.separator + "uploads" + File.separator;

                File dir = new File(uploadDir);
                if (!dir.exists()) {
                    dir.mkdirs();
                }

                if (existingUser != null && existingUser.getAvatar() != null && !existingUser.getAvatar().isEmpty()) {
                    Files.deleteIfExists(Paths.get(uploadDir + existingUser.getAvatar()));
                }

                String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
                Files.write(Paths.get(uploadDir + fileName), imageFile.getBytes());
                user.setAvatar(fileName);
            } catch (IOException e) {
                throw new RuntimeException("Error al guardar la imagen: " + e.getMessage(), e);
            }
        } else if (existingUser != null) {
            user.setAvatar(existingUser.getAvatar());
        }
    }

    @Override
    @Transactional
    public void updatePerfil(String username, Usuario datosActualizados, MultipartFile imagen) {
        Usuario usuarioBD = usuarioDAO.findByNombreUsuario(username);
        if (usuarioBD == null) {
            throw new RuntimeException("Usuario no encontrado: " + username);
        }

        usuarioBD.setNombre(datosActualizados.getNombre());
        usuarioBD.setPrimerApellido(datosActualizados.getPrimerApellido());
        usuarioBD.setEmail(datosActualizados.getEmail());
        usuarioBD.setTelefono(datosActualizados.getTelefono());

        if (datosActualizados.getPassword() != null && !datosActualizados.getPassword().trim().isEmpty()) {
            usuarioBD.setPassword(passwordEncoder.encode(datosActualizados.getPassword()));
        }

        handleUserImage(usuarioBD, imagen, usuarioBD);

        usuarioDAO.save(usuarioBD);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> getJugadoresConGenero(Long categoriaId) {
        Categoria categoria = categoriaDAO.findById(categoriaId)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada: " + categoriaId));

        Rol rolJugador = rolDAO.findByNombreRol("ROLE_JUGADOR")
                .orElseThrow(() -> new IllegalStateException("No existe el rol 'ROLE_JUGADOR'"));

        List<Usuario> todosJugadores = usuarioDAO.findByRoles(rolJugador);

        List<Long> idsYaAsignados = usuarioCategoriaDAO.findByCategoria_Id(categoriaId)
                .stream()
                .map(uc -> uc.getUsuario().getId())
                .toList();

        return todosJugadores.stream()
                .filter(u -> !idsYaAsignados.contains(u.getId()))
                .filter(u -> {
                    if (categoria.getGenero().name().equalsIgnoreCase("MIXTO")) {
                        return true;
                    }
                    return u.getGenero() != null &&
                            u.getGenero().name().equals(categoria.getGenero().name());
                })
                .toList();
    }
}