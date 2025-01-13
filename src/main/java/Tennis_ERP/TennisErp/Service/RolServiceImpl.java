package Tennis_ERP.TennisErp.Service;

import Tennis_ERP.TennisErp.DAO.rolDAO;
import Tennis_ERP.TennisErp.domain.rol;
import Tennis_ERP.TennisErp.service.RolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RolServiceImpl implements RolService {

    private final rolDAO rolDAO;

    @Autowired
    public RolServiceImpl(rolDAO rolDAO) {
        this.rolDAO = rolDAO;
    }

    @Override
    public List<rol> findAllRoles() {
        return rolDAO.findAll();  // Obtiene todos los roles desde la base de datos
    }

    @Override
    public rol findRolByType(rol.RoleType roleType) {
        return rolDAO.findByRol(roleType);  // Obtiene un rol por su tipo
    }

    @Override
    public rol saveRol(rol rol) {
        return rolDAO.save(rol);  // Guarda o actualiza el rol en la base de datos
    }

    @Override
    public rol findRolById(int id) {
        // Busca un rol por ID utilizando Optional
        Optional<rol> rol = rolDAO.findById(id);
        return rol.orElse(null);  // Si el rol no existe, retorna null
    }

    @Override
    public void deleteRol(int id) {
        // Elimina el rol por ID
        rolDAO.deleteById(id);
    }
}
