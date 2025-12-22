/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Tennis_ERP.TennisErp.controller;

import Tennis_ERP.TennisErp.service.RolService;
import Tennis_ERP.TennisErp.service.UsuarioService;
import Tennis_ERP.TennisErp.validations.ValidationGroups;
import Tennis_ERP.TennisErp.validations.ValidationGroups.*;
import Tennis_ERP.TennisErp.service.UsuarioService;
import Tennis_ERP.TennisErp.dao.RolDAO;
import Tennis_ERP.TennisErp.domain.Rol;
import Tennis_ERP.TennisErp.domain.Usuario;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;
import java.util.Set;

@Controller
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private RolDAO rolDAO;

    @Autowired
    private RolService rolService;

    @GetMapping("/menuAdmin/usuarios")
    public String listarTodosLosUsuarios(Model model) {
        List<Usuario> usuarios = usuarioService.getAllUsuarios(); // Este método devuelve todos sin filtrar
        model.addAttribute("usuarios", usuarios);
        return "usuariosLista";
    }

    @GetMapping("/menuAdmin/usuarios/nuevo")
    public String mostrarFormularioNuevoUsuario(Model model) {
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("roles", rolDAO.findAll());
        return "usuariosCrear";
    }

    @PostMapping("/menuAdmin/usuarios/guardar")
    public String guardarUsuario(
            @Validated(OnCreate.class) @ModelAttribute("usuario") Usuario usuario,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (usuarioService.findByNombreUsuario(usuario.getNombreUsuario()).isPresent()) {
            result.rejectValue("nombreUsuario", "error.usuario", "El nombre de usuario ya está en uso");
        }

        if (usuarioService.findByDni(usuario.getDni()).isPresent()) {
            result.rejectValue("dni", "error.usuario", "El DNI ya está registrado");
        }

        if (usuario.getRoles() == null || usuario.getRoles().isEmpty()) {
            result.rejectValue("roles", "error.usuario", "Debe seleccionar al menos un rol");
        }
        

        if (result.hasErrors()) {
            model.addAttribute("roles", rolDAO.findAll());
            return "usuariosCrear";
        }

        usuarioService.saveUsuario(usuario);
        redirectAttributes.addFlashAttribute("successMessage", "Usuario creado exitosamente.");
        return "redirect:/menuAdmin/usuarios";
    }

    @GetMapping("/menuAdmin/usuarios/editar/{id}")
    public String mostrarFormularioEditarUsuario(@PathVariable Long id, Model model) {
        Usuario usuario = usuarioService.getUsuarioById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID no válido: " + id));

        model.addAttribute("usuario", usuario);
        model.addAttribute("rolesDisponibles", rolService.getAllRoles());

        return "usuariosEditar";
    }

    @PostMapping("/menuAdmin/usuarios/actualizar/{id}")
    public String actualizarUsuario(@PathVariable Long id,
            @Validated(OnUpdate.class) @ModelAttribute("usuario") Usuario usuario,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute("rolesDisponibles", rolService.getAllRoles());
            return "usuariosEditar";
        }

        Usuario original = usuarioService.getUsuarioById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID no válido: " + id));

        // Validación de email, nombreUsuario y DNI si cambian
        if (!usuario.getEmail().equals(original.getEmail()) && usuarioService.findByEmail(usuario.getEmail()).isPresent()) {
            result.rejectValue("email", "error.usuario", "El email ya está en uso");
        }

        if (!usuario.getNombreUsuario().equals(original.getNombreUsuario()) && usuarioService.findByNombreUsuario(usuario.getNombreUsuario()).isPresent()) {
            result.rejectValue("nombreUsuario", "error.usuario", "El nombre de usuario ya está en uso");
        }

        if (!usuario.getDni().equals(original.getDni()) && usuarioService.findByDni(usuario.getDni()).isPresent()) {
            result.rejectValue("dni", "error.usuario", "El DNI ya está registrado");
        }

        if (result.hasErrors()) {
            model.addAttribute("rolesDisponibles", rolService.getAllRoles());
            return "usuariosEditar";
        }

        // Conservar relaciones que no vienen del formulario
        usuario.setUsuarioCategorias(original.getUsuarioCategorias());

        // Contraseña: solo cifrar si se ha modificado
        if (usuario.getPassword() == null || usuario.getPassword().isBlank()) {
            usuario.setPassword(original.getPassword());
        } else {
            usuario.setPassword(usuarioService.encodePassword(usuario.getPassword()));
        }

        usuarioService.saveUsuario(usuario);
        redirectAttributes.addFlashAttribute("successMessage", "Usuario actualizado correctamente.");

        return "redirect:/menuAdmin/usuarios";
    }

    @GetMapping("/menuAdmin/usuarios/eliminar/{id}")
    public String eliminarUsuario(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        usuarioService.deleteUsuario(id);
        redirectAttributes.addFlashAttribute("successMessage", "Usuario eliminado correctamente.");
        return "redirect:/menuAdmin/usuarios";
    }
}
