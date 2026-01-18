package Tennis_ERP.TennisErp.controller;

import Tennis_ERP.TennisErp.domain.Usuario;
import Tennis_ERP.TennisErp.service.FileUploadService;
import Tennis_ERP.TennisErp.service.RolService;
import Tennis_ERP.TennisErp.service.UsuarioService;
import jakarta.validation.Valid;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private RolService rolService;

    @Autowired
    private FileUploadService fileUploadService;

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

    @GetMapping("/usuarios/nuevo")
    public String formularioNuevoUsuario(Model model) {
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("roles", rolService.getAllRoles());
        return "usuarios/gestion_usuario/usuarios_crear";
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
    public String guardarUsuario(@Valid @ModelAttribute("usuario") Usuario usuario,
            BindingResult bindingResult,
            @RequestParam(value = "avatarFile", required = false) MultipartFile imagen,
            Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("roles", rolService.getAllRoles());
            // Al devolver el string, Spring mantiene el objeto 'usuario' en el Model
            return "usuarios/gestion_usuario/usuarios_crear";
        }

        try {
            if (imagen != null && !imagen.isEmpty()) {
                usuarioService.saveUsuarioWithImage(usuario, imagen);
            } else {
                usuarioService.saveUsuario(usuario);
            }
            return "redirect:/usuarios";
        } catch (Exception e) {
            model.addAttribute("errorDni", "ERROR: DNI o Usuario duplicado.");
            model.addAttribute("roles", rolService.getAllRoles());
            return "usuarios/gestion_usuario/usuarios_crear";
        }
    }

    @PostMapping("/jugadores/guardar")
    public String guardarJugador(@ModelAttribute("jugador") Usuario usuario,
            @RequestParam(value = "imagen", required = false) MultipartFile imagen,
            Model model) {
        try {
            if (imagen != null && !imagen.isEmpty()) {
                usuarioService.saveUsuarioWithImage(usuario, imagen);
            } else {
                usuarioService.saveUsuario(usuario);
            }
            return "redirect:/jugadores";
        } catch (Exception e) {
            model.addAttribute("errorDni", "ERROR: DNI o Usuario duplicado.");
            model.addAttribute("roles", rolService.getAllRoles());
            return "usuarios/gestion_jugadores/jugadores_crear";
        }
    }

    @PostMapping("/usuarios/actualizar/{id}")
    public String actualizarUsuario(@PathVariable Long id,
            @ModelAttribute("usuario") Usuario form,
            @RequestParam(value = "imagen", required = false) MultipartFile imagen,
            Model model) {
        try {
            if (imagen != null && !imagen.isEmpty()) {
                usuarioService.updateUsuarioWithImage(id, form, imagen);
            } else {
                usuarioService.updateUsuario(id, form);
            }
            return "redirect:/usuarios";
        } catch (Exception e) {
            prepararModeloError(model);
            return "usuarios/gestion_usuario/usuarios_editar";
        }
    }

    @PostMapping("/jugadores/actualizar/{id}")
    public String actualizarJugador(@PathVariable Long id,
            @ModelAttribute("usuario") Usuario form,
            @RequestParam(value = "imagen", required = false) MultipartFile imagen,
            Model model) {
        try {
            if (imagen != null && !imagen.isEmpty()) {
                usuarioService.updateUsuarioWithImage(id, form, imagen);
            } else {
                usuarioService.updateUsuario(id, form);
            }
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
            @RequestParam(value = "archivoImagen", required = false) MultipartFile imagen,
            Principal principal,
            RedirectAttributes redirectAttributes) {

        try {
            usuarioService.updatePerfil(principal.getName(), datosActualizados, imagen);
            redirectAttributes.addFlashAttribute("mensaje", "Perfil actualizado con éxito");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar el perfil: " + e.getMessage());
        }

        return "redirect:/perfil";
    }
}