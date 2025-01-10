package Tennis_ERP.TennisErp.service;

import Tennis_ERP.TennisErp.domain.Trabajador;
import Tennis_ERP.TennisErp.domain.rol;

import java.util.List;
import java.util.Optional;

public interface TrabajadorService {

    // Método para guardar o actualizar un trabajador
    Trabajador saveTrabajador(Trabajador trabajador);

    // Método para obtener todos los trabajadores
    List<Trabajador> getAllTrabajadores();

    // Método para obtener un trabajador por su ID
    Optional<Trabajador> getTrabajadorById(Long id);

    // Método para eliminar un trabajador por su ID
    void deleteTrabajador(Long id);

    // Método para obtener todos los roles
    List<rol> getAllRoles();

    // Método para buscar trabajador por DNI
    Optional<Trabajador> getTrabajadorByDni(String dni);

    // Método para buscar trabajadores por correo
    List<Trabajador> getTrabajadoresByCorreo(String correo);
}