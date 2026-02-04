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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private RolService rolService;

    @Override
    @Transactional
    public Usuario saveUsuario(Usuario user) {
        // 1. Encriptar contraseña si es necesario
        if (user.getPassword() != null && !user.getPassword().startsWith("$2a$")) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        // 2. SINCRONIZAR ROLES (Solución al problema)
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

        // SINCRONIZAR ROLES
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

        // Eliminar también la imagen del avatar si existe
        if (usuario.getAvatar() != null && !usuario.getAvatar().isEmpty()) {
            String uploadDir = System.getProperty("user.dir") + "/uploads/";
            try {
                Files.deleteIfExists(Paths.get(uploadDir + usuario.getAvatar()));
            } catch (IOException e) {
                // Log de error pero no detenemos la operación
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
        // 1. Obtener el rol "Jugador"
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
    public void updateUsuario(Long id, Usuario form, Long rolId) {
        // 1. Buscamos el usuario real en la DB
        Usuario db = usuarioDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));

        // 2. Protegemos al Admin principal (opcional)
        if ("admin".equals(db.getNombreUsuario()) && rolId == null) {
            throw new RuntimeException("El administrador principal debe tener un rol asignado.");
        }

        // 3. Sincronizamos campos básicos y los nuevos que añadiste
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
        db.setAvatar(form.getAvatar());
        db.setEmail(form.getEmail());
        db.setTelefono(form.getTelefono());

        // 4. ACTUALIZACIÓN DE ROL: Buscamos el Rol por ID y lo asignamos
        if (rolId != null) {
            Rol rolSeleccionado = rolDAO.findById(rolId)
                    .orElseThrow(() -> new RuntimeException("El Rol seleccionado no existe"));

            Set<Rol> nuevosRoles = new HashSet<>();
            nuevosRoles.add(rolSeleccionado);
            db.setRoles(nuevosRoles);
        }

        // 5. Gestión de Password (solo si el usuario escribió algo en el campo)
        if (form.getPassword() != null && !form.getPassword().trim().isEmpty()) {
            db.setPassword(passwordEncoder.encode(form.getPassword()));
        }

        // 6. Guardamos los cambios
        usuarioDAO.save(db);
    }

    @Override
    @Transactional
    public void updateUsuarioWithImage(Long id, Usuario form, MultipartFile imageFile, Long rolId) {
        // 1. Buscar usuario existente
        Usuario db = usuarioDAO.findById(id).orElseThrow(() -> new RuntimeException("No existe"));

        // 2. Sincronización de campos básicos (DNI, Email, Teléfono, etc.)
        db.setNombreUsuario(form.getNombreUsuario());
        db.setNombre(form.getNombre());
        db.setPrimerApellido(form.getPrimerApellido());
        db.setSegundoApellido(form.getSegundoApellido());
        db.setDni(form.getDni());
        db.setEmail(form.getEmail());
        db.setTelefono(form.getTelefono());

        // CAMPOS ADICIONALES DE TU FORMULARIO
        db.setDniFamiliar(form.getDniFamiliar());
        db.setGenero(form.getGenero());
        db.setFechaNacimiento(form.getFechaNacimiento());
        db.setMatricula(form.getMatricula());
        db.setFormaDePago(form.getFormaDePago());

        // 3. Lógica de ROL (Evita que se pierda)
        if (rolId != null) {
            Rol rolSeleccionado = rolDAO.findById(rolId)
                    .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

            // Verificación de seguridad para ADMIN
            if ("admin".equals(db.getNombreUsuario()) && !rolSeleccionado.getNombreRol().equals("ROLE_ADMIN")) {
                throw new RuntimeException("PROTECCIÓN: No puedes quitar el rol ROLE_ADMIN al usuario principal.");
            }

            Set<Rol> nuevosRoles = new HashSet<>();
            nuevosRoles.add(rolSeleccionado);
            db.setRoles(nuevosRoles);
        }

        // 4. Lógica de contraseña
        if (form.getPassword() != null && !form.getPassword().trim().isEmpty()) {
            db.setPassword(passwordEncoder.encode(form.getPassword()));
        }

        // 5. Procesar imagen
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                // Suponiendo que handleUserImage sube la foto y devuelve la URL o la asigna al
                // objeto
                // Si handleUserImage ya hace db.setAvatar(url), déjalo así:
                handleUserImage(db, imageFile, db);
            } catch (Exception e) {
                throw new RuntimeException("Error al procesar la imagen: " + e.getMessage());
            }
        } else {
            // Si no sube imagen nueva, mantenemos la que ya tenía (form.getAvatar() o
            // db.getAvatar())
            db.setAvatar(form.getAvatar());
        }

        usuarioDAO.save(db);
    }

    private void handleUserImage(Usuario user, MultipartFile imageFile, Usuario existingUser) {
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/images/";

                // Crear carpeta si no existe
                java.io.File dir = new java.io.File(uploadDir);
                if (!dir.exists()) {
                    dir.mkdirs();
                }

                // Eliminar imagen anterior si existe
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
            // Mantener la imagen existente si no se sube una nueva
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

        // Actualizar campos básicos
        usuarioBD.setNombre(datosActualizados.getNombre());
        usuarioBD.setPrimerApellido(datosActualizados.getPrimerApellido());
        usuarioBD.setEmail(datosActualizados.getEmail());
        usuarioBD.setTelefono(datosActualizados.getTelefono());

        // Actualizar contraseña si se proporciona
        if (datosActualizados.getPassword() != null && !datosActualizados.getPassword().trim().isEmpty()) {
            usuarioBD.setPassword(passwordEncoder.encode(datosActualizados.getPassword()));
        }

        // Procesar imagen si se proporciona
        handleUserImage(usuarioBD, imagen, usuarioBD);

        usuarioDAO.save(usuarioBD);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> getJugadoresConGenero(Long categoriaId) {
        // 1. Obtener la categoría
        Categoria categoria = categoriaDAO.findById(categoriaId)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada: " + categoriaId));

        // 2. Obtener el rol "Jugador"
        Rol rolJugador = rolDAO.findByNombreRol("ROLE_JUGADOR")
                .orElseThrow(() -> new IllegalStateException("No existe el rol 'ROLE_JUGADOR'"));

        // 3. Obtener todos los usuarios con ese rol
        List<Usuario> todosJugadores = usuarioDAO.findByRoles(rolJugador);

        // 4. Obtener IDs de usuarios ya asignados
        List<Long> idsYaAsignados = usuarioCategoriaDAO.findByCategoria_Id(categoriaId)
                .stream()
                .map(uc -> uc.getUsuario().getId())
                .toList();

        // 5. Filtrar con lógica para MIXTO
        return todosJugadores.stream()
                .filter(u -> !idsYaAsignados.contains(u.getId()))
                .filter(u -> {
                    // Si la categoría es MIXTA, permitimos todos los géneros
                    if (categoria.getGenero().name().equalsIgnoreCase("MIXTO")) {
                        return true;
                    }
                    // Si no es mixta, el género del jugador debe coincidir exactamente
                    return u.getGenero() != null &&
                            u.getGenero().name().equals(categoria.getGenero().name());
                })
                .toList();
    }
}