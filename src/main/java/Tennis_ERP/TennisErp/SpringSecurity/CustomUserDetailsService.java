package Tennis_ERP.TennisErp.SpringSecurity;

import Tennis_ERP.TennisErp.DAO.UsuarioDAO;
import Tennis_ERP.TennisErp.domain.Usuario;
import java.util.Optional;
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
import java.util.Set;
import java.util.stream.Collectors;

@Service("userDetailsService")
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioDAO usuarioDAO;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String nombreUsuario) throws UsernameNotFoundException {

        Usuario usuario = usuarioDAO.findByNombreUsuario(nombreUsuario);

        if (usuario == null) {
            throw new UsernameNotFoundException("Usuario no encontrado: " + nombreUsuario);
        }

        Set<GrantedAuthority> authorities = usuario.getRoles().stream()
                .map(rol -> new SimpleGrantedAuthority(rol.getNombreRol()))
                .collect(Collectors.toSet());

        log.info("Usuario: {}", usuario.getNombreUsuario());
        log.info("Roles: {}", authorities);

        return new User(usuario.getNombreUsuario(), usuario.getPassword(), authorities);
    }
}
