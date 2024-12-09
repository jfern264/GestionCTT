package Tennis_ERP.TennisErp.Service;

import Tennis_ERP.TennisErp.domain.usuario;
import Tennis_ERP.TennisErp.domain.rol;
import Tennis_ERP.TennisErp.DAO.UsuarioDAO;
import java.util.ArrayList;
import java.util.Collections;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Collections;
import java.util.Optional;

@Service("CustomUserDetailsService")
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioDAO usuarioDao;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String nombre) throws UsernameNotFoundException {

        Optional<usuario> optionalUsuario = usuarioDao.findByNombre(nombre);

        // Si no existe el usuario, lanzamos excepción
        usuario usuario = optionalUsuario.orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + nombre));

        // Obtenemos el rol del usuario
        rol rol = usuario.getRol();
        GrantedAuthority autoridad = new SimpleGrantedAuthority(rol.getRol());

        // Log de información
        log.info("Usuario: {}", usuario.getNombre());
        log.info("Contraseña: {}", usuario.getContraseña());
        log.info("Rol: {}", autoridad.getAuthority());

        // Retornamos el usuario con nombre de usuario, contraseña y rol
        return new User(usuario.getNombre(), usuario.getContraseña(), Collections.singleton(autoridad));
    }
}
