package Tennis_ERP.TennisErp.Service;

import Tennis_ERP.TennisErp.domain.Rol;
import Tennis_ERP.TennisErp.service.RolService;
import Tennis_ERP.TennisErp.DAO.RolDAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class RolServiceImpl implements RolService {

    @Autowired
    private RolDAO rolDAO;

    @Override
    @Transactional
    public Rol saveRol(Rol rol) {
        return rolDAO.save(rol);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Rol> getAllRoles() {
        return rolDAO.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Rol> getRolById(Long id) {
        return rolDAO.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Rol> findByNombreRol(String nombreRol) {
        return Optional.ofNullable(rolDAO.findByNombreRol(nombreRol));
    }

    @Override
    @Transactional
    public void deleteRol(Long id) {
        rolDAO.deleteById(id);
    }
}