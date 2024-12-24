package Tennis_ERP.TennisErp.Service;

import Tennis_ERP.TennisErp.domain.usuario;

public interface UsuarioService {

    void crearUsuario(usuario nuevoUsuario);

    usuario obtenerUsuarioPorNombre(String nombre);

    boolean verificarContraseña(String contrasenaIngresada, String contrasenaCifrada);

    void actualizarUsuario(usuario usuarioActualizado);

    void eliminarUsuarioPorId(int id);
}
