package Tennis_ERP.TennisErp.Service;

import Tennis_ERP.TennisErp.DAO.EntrenadorDAO;
import Tennis_ERP.TennisErp.service.EntrenadorService;
import Tennis_ERP.TennisErp.domain.Entrenador;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EntrenadorServiceImpl implements EntrenadorService {

    private EntrenadorDAO entrenadorDAO;
    
    @Autowired
    public EntrenadorServiceImpl(EntrenadorDAO entrenadorDAO) {
        this.entrenadorDAO = entrenadorDAO;
    }
    
    @Override
    public Entrenador crearEntrenador(Entrenador entrenador) {
        return entrenadorDAO.save(entrenador);
    }

    @Override
    public List<Entrenador> listarEntrenadores() {
        return entrenadorDAO.findAll();
    }

    @Override
    public Optional<Entrenador> obtenerEntrenadorPorId(Long id) {
    return entrenadorDAO.findById(id);
}

    @Override
    public void eliminarEntrenador(Long id) {
        entrenadorDAO.deleteById(id);
    }
    
    @Override
    public Entrenador actualizarEntrenador(Entrenador entrenador) {
        return entrenadorDAO.save(entrenador);
    }
}