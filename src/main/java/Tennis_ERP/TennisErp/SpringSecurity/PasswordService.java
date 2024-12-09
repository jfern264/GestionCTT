package Tennis_ERP.TennisErp.SpringSecurity;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordService {

    private final PasswordEncoder passwordEncoder;

    // Constructor para inyectar el PasswordEncoder
    public PasswordService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    // Método para cifrar una contraseña
    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    // Método para verificar la contraseña
    public boolean matches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
