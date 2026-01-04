package Tennis_ERP.TennisErp.service;

import java.util.List;
import java.util.Optional;

import Tennis_ERP.TennisErp.domain.Usuario;

public interface UsuarioService {

    Usuario saveUsuario(Usuario usuario);

    List<Usuario> getAllUsuarios();

    Optional<Usuario> getUsuarioById(Long id);

    Optional<Usuario> findByNombreUsuario(String nombreUsuario);

    Optional<Usuario> findByEmail(String email);

    void deleteUsuario(Long id);

    List<Usuario> getUsuariosPorRol(String nombreRol);

    Optional<Usuario> findByDni(String dni);

    String encodePassword(String rawPassword);

    List<Usuario> getJugadoresDisponibles(Long categoriaId);
    
    void updateUsuario(Long id, Usuario form);
}
