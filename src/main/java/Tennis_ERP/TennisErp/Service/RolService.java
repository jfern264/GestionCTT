package Tennis_ERP.TennisErp.service;

import Tennis_ERP.TennisErp.domain.rol;
import java.util.List;

public interface RolService {
    
    List<rol> findAllRoles(); // Para obtener todos los roles
    
    rol findRolById(int id);  // Para obtener un rol por su ID
    
    rol findRolByType(rol.RoleType roleType);  // Para obtener un rol por su tipo
    
    rol saveRol(rol rol); // Para guardar o actualizar un rol
    
    void deleteRol(int id);  // Para eliminar un rol por ID
}
