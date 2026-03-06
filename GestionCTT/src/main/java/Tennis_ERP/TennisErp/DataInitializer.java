package Tennis_ERP.TennisErp;

import Tennis_ERP.TennisErp.dao.RolDAO;
import Tennis_ERP.TennisErp.dao.UsuarioDAO;
import Tennis_ERP.TennisErp.domain.Rol;
import Tennis_ERP.TennisErp.domain.Usuario;
import Tennis_ERP.TennisErp.resources.Genero;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private RolDAO rolRepository;
    
    @Autowired
    private UsuarioDAO usuarioRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        System.out.println("🔍 Inicializando datos de la aplicación...");
        
        // 1. Verificar y completar roles faltantes
        verificarYCompletarRoles();
        
        // 2. Verificar y crear administrador si es necesario
        verificarYCrearAdministrador();
        
        System.out.println("✅ Inicialización completada.");
    }
    
    private void verificarYCompletarRoles() {
        String[] rolesRequeridos = {"ROLE_ADMIN", "ROLE_JUGADOR", "ROL_EMPLEADO"};
        
        // Obtener todos los roles existentes
        List<Rol> rolesExistentes = rolRepository.findAll();
        Set<String> nombresRolesExistentes = new HashSet<>();
        
        for (Rol rol : rolesExistentes) {
            nombresRolesExistentes.add(rol.getNombreRol());
        }
        
        // Verificar qué roles faltan
        int rolesCreados = 0;
        for (String nombreRol : rolesRequeridos) {
            if (!nombresRolesExistentes.contains(nombreRol)) {
                Rol nuevoRol = new Rol();
                nuevoRol.setNombreRol(nombreRol);
                rolRepository.save(nuevoRol);
                rolesCreados++;
                System.out.println("➕ Rol creado: " + nombreRol);
            }
        }
        
        if (rolesCreados > 0) {
            System.out.println("✅ Se crearon " + rolesCreados + " roles faltantes.");
        } else {
            System.out.println("✓ Todos los roles requeridos ya existen.");
        }
    }
    
    private void verificarYCrearAdministrador() {
        // Verificar si existe ALGÚN usuario en el sistema
        long totalUsuarios = usuarioRepository.count();
        
        if (totalUsuarios == 0) {
            // Caso 1: No hay ningún usuario - crear admin
            crearAdministrador();
            System.out.println("🏁 No había usuarios. Se creó el administrador inicial.");
            
        } else if (!usuarioRepository.existsByNombreUsuario("admin")) {
            // Caso 2: Hay usuarios pero no existe el admin - crear admin
            crearAdministrador();
            System.out.println("👤 No existía usuario 'admin'. Se creó el administrador.");
            
        } else {
            System.out.println("✓ El usuario administrador ya existe.");
        }
    }
    
    private void crearAdministrador() {
        Usuario admin = new Usuario();
        admin.setNombreUsuario("admin");
        admin.setPassword(passwordEncoder.encode("1"));
        admin.setDni("10000000T");
        admin.setEmail("admin@cttplatinum.com");
        admin.setNombre("Administrador");
        admin.setPrimerApellido("Sistema");
        admin.setSegundoApellido("CTT");
        admin.setTelefono("666777888");
        admin.setMatricula("ADMIN-001");
        admin.setFormaDePago("N/A");
        
        // Buscar y asignar rol ADMIN
        Rol rolAdmin = rolRepository.findByNombreRol("ROLE_ADMIN")
            .orElseGet(() -> {
                // Si por alguna razón no existe, lo creamos sobre la marcha
                Rol nuevoRol = new Rol();
                nuevoRol.setNombreRol("ROLE_ADMIN");
                System.out.println("⚠️ Rol ADMIN no encontrado, creándolo automáticamente...");
                return rolRepository.save(nuevoRol);
            });
        
        Set<Rol> roles = new HashSet<>();
        roles.add(rolAdmin);
        admin.setRoles(roles);
        
        usuarioRepository.save(admin);
        System.out.println("✅ Usuario administrador creado:");
        System.out.println("   └─ Usuario: admin");
        System.out.println("   └─ Contraseña: admin123");
        System.out.println("   └─ Email: admin@cttplatinum.com");
    }
}