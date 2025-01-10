package Tennis_ERP.TennisErp.service;

import Tennis_ERP.TennisErp.DAO.TrabajadorDAO;
import Tennis_ERP.TennisErp.DAO.rolDAO;
import Tennis_ERP.TennisErp.domain.Trabajador;
import Tennis_ERP.TennisErp.domain.rol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TrabajadorServiceImpl implements TrabajadorService {

    @Autowired
    private TrabajadorDAO trabajadorDAO;

    @Autowired
    private rolDAO rolDAO;  // Suponiendo que tienes un DAO para manejar roles

    @Override
    public Trabajador saveTrabajador(Trabajador trabajador) {
        return trabajadorDAO.save(trabajador);
    }

    @Override
    public List<Trabajador> getAllTrabajadores() {
        return trabajadorDAO.findAll();
    }

    @Override
    public Optional<Trabajador> getTrabajadorById(Long id) {
        return trabajadorDAO.findById(id);
    }

    @Override
    public void deleteTrabajador(Long id) {
        trabajadorDAO.deleteById(id);
    }

    @Override
    public List<rol> getAllRoles() {
        return rolDAO.findAll();  // Si existe un DAO para roles
    }

    @Override
    public Optional<Trabajador> getTrabajadorByDni(String dni) {
        return Optional.ofNullable(trabajadorDAO.findByDni(dni));
    }

    @Override
    public List<Trabajador> getTrabajadoresByCorreo(String correo) {
        return trabajadorDAO.findByCorreo(correo);
    }
}
