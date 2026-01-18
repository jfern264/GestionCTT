package Tennis_ERP.TennisErp.controller;

import Tennis_ERP.TennisErp.domain.Usuario;
import Tennis_ERP.TennisErp.service.RolService;
import Tennis_ERP.TennisErp.service.UsuarioService;

import java.security.Principal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private RolService rolService;

    private Object flash;

    // ==========================================
    // 1. LISTADOS
    // ==========================================
    @GetMapping("/usuarios")
    public String listarTodos(Model model) {
        model.addAttribute("usuarios", usuarioService.getAllUsuarios());
        return "usuarios/gestion_usuario/usuarios_lista";
    }

    @GetMapping("/jugadores")
    public String listarJugadores(Model model) {
        model.addAttribute("jugadores", usuarioService.getUsuariosPorRol("ROLE_JUGADOR"));
        return "usuarios/gestion_jugadores/jugadores_lista";
    }

    @GetMapping("/trabajadores")
    public String listarTrabajadores(Model model) {
        // Asegúrate de que el rol se llame exactamente así en tu DB
        model.addAttribute("trabajadores", usuarioService.getUsuariosPorRol("ROLE_TRABAJADOR"));
        return "usuarios/gestion_trabajador/trabajadores_lista";
    }

    // ==========================================
    // 2. FORMULARIOS DE ALTA
    // ==========================================
    @GetMapping("/jugadores/nuevo")
    public String formularioNuevoJugador(Model model) {

        model.addAttribute("jugador", new Usuario());
        model.addAttribute("roles", rolService.getAllRoles());

        return "usuarios/gestion_jugadores/jugadores_crear";
    }


    // ==========================================
    // 3. FORMULARIOS DE EDICIÓN
    // ==========================================
    @GetMapping("/usuarios/editar/{id}")
    public String formularioEditarUsuario(@PathVariable Long id, Model model) {
        Usuario user = usuarioService.getUsuarioById(id).orElseThrow();
        model.addAttribute("usuario", user);
        model.addAttribute("rolesDisponibles", rolService.getAllRoles());
        return "usuarios/gestion_usuario/usuarios_editar";
    }

    @GetMapping("/jugadores/editar/{id}")
    public String formularioEditarJugador(@PathVariable Long id, Model model) {
        Usuario user = usuarioService.getUsuarioById(id).orElseThrow();
        // Unificado a "usuario" para que los campos del HTML carguen correctamente
        model.addAttribute("usuario", user);
        model.addAttribute("rolesDisponibles", rolService.getAllRoles());
        return "usuarios/gestion_jugadores/jugadores_editar";
    }

    // ==========================================
    // 4. ACCIONES DE PERSISTENCIA
    // ==========================================

    @PostMapping("/usuarios/guardar")
    public String guardarUsuario(@ModelAttribute("usuario") Usuario usuario, Model model) {
        try {
            usuarioService.saveUsuario(usuario);
            return "redirect:/usuarios";
        } catch (Exception e) {
            model.addAttribute("errorDni", "ERROR: DNI o Usuario duplicado.");
            model.addAttribute("roles", rolService.getAllRoles());
            return "usuarios/gestion_usuario/usuarios_crear";
        }
    }

    @PostMapping("/usuarios/actualizar/{id}")
    public String actualizarUsuario(@PathVariable Long id, @ModelAttribute("usuario") Usuario form, Model model) {
        try {
            usuarioService.updateUsuario(id, form);
            return "redirect:/usuarios";
        } catch (Exception e) {
            prepararModeloError(model);
            return "usuarios/gestion_usuario/usuarios_editar";
        }
    }

    @PostMapping("/jugadores/actualizar/{id}")
    public String actualizarJugador(@PathVariable Long id, @ModelAttribute("usuario") Usuario form, Model model) {
        try {
            usuarioService.updateUsuario(id, form);
            return "redirect:/jugadores";
        } catch (Exception e) {
            prepararModeloError(model);
            return "usuarios/gestion_jugadores/jugadores_editar";
        }
    }

    @GetMapping("/usuarios/eliminar/{id}")
    public String eliminarUsuario(@PathVariable Long id, RedirectAttributes flash) {
        try {
            usuarioService.deleteUsuario(id);
            flash.addFlashAttribute("success", "Usuario maestro eliminado correctamente.");
        } catch (RuntimeException e) {
            // Aquí capturamos el mensaje de "OPERACIÓN DENEGADA" del servicio
            flash.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/usuarios";
    }

    @GetMapping("/jugadores/eliminar/{id}")
    public String eliminarJugador(@PathVariable Long id, RedirectAttributes flash) {
        try {
            usuarioService.deleteUsuario(id);
            flash.addFlashAttribute("success", "Jugador retirado del sistema.");
        } catch (RuntimeException e) {
            flash.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/jugadores";
    }

    // Helper para no repetir código en los catch de actualización
    private void prepararModeloError(Model model) {
        model.addAttribute("errorDni", "IDENTIDAD DUPLICADA: Verifique DNI, Email o Usuario.");
        model.addAttribute("rolesDisponibles", rolService.getAllRoles());
    }

    @GetMapping("/perfil")
    public String verPerfil(Model model, Principal principal) {
        // 1. Obtenemos el username del usuario logueado
        String username = principal.getName();

        // 2. Buscamos los datos completos del socio en la BD
        // Usamos .orElseThrow() para extraer el Usuario o lanzar un error si no existe
        Usuario usuario = usuarioService.findByNombreUsuario(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + username));

        // 3. Ahora pasamos el objeto Usuario (ya no es un Optional)
        model.addAttribute("usuario", usuario);

        // 4. IMPORTANTE: He quitado el espacio extra al final de "perfil "
        return "usuarios/gestion_usuario/perfil";
    }
    @PostMapping("/perfil/guardar")
public String guardarPerfil(@ModelAttribute("usuario") Usuario datosActualizados, 
                             Principal principal, 
                             RedirectAttributes redirectAttributes) {
    
    // 1. Buscamos el usuario real en la BD usando el Principal por seguridad
    Usuario usuarioBD = usuarioService.findByNombreUsuario(principal.getName())
                        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

    // 2. Actualizamos solo los campos permitidos (evitamos que cambien roles o ID)
    usuarioBD.setNombre(datosActualizados.getNombre());
    usuarioBD.setPrimerApellido(datosActualizados.getPrimerApellido());
    usuarioBD.setSegundoApellido(datosActualizados.getSegundoApellido());
    usuarioBD.setEmail(datosActualizados.getEmail());
    usuarioBD.setTelefono(datosActualizados.getTelefono());

    // 3. Gestión de la contraseña (solo si el usuario escribió algo)
    if (datosActualizados.getPassword() != null && !datosActualizados.getPassword().isEmpty()) {
        // IMPORTANTE: Aquí deberías usar tu passwordEncoder si tienes uno configurado
        // usuarioBD.setPassword(passwordEncoder.encode(datosActualizados.getPassword()));
        usuarioBD.setPassword(datosActualizados.getPassword()); 
    }

    // 4. Guardamos los cambios
    usuarioService.saveUsuario(usuarioBD);

    // 5. Mensaje de éxito y redirección
    redirectAttributes.addFlashAttribute("mensaje", "Perfil actualizado correctamente");
    return "redirect:/perfil";
}
}