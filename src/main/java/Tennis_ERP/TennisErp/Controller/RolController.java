/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Tennis_ERP.TennisErp.Controller;

import Tennis_ERP.TennisErp.DAO.RolDAO;
import Tennis_ERP.TennisErp.Service.UsuarioService;
import Tennis_ERP.TennisErp.Validations.ValidationGroups.*;
import Tennis_ERP.TennisErp.Domain.Rol;
import Tennis_ERP.TennisErp.Domain.Usuario;
import Tennis_ERP.TennisErp.service.RolService;
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

/**
 *
 * @author Usuario
 */
@Controller
@RequestMapping("/menuAdmin/roles")
public class RolController {

    @Autowired
    private RolService rolService;

    @GetMapping
    public String listarRoles(Model model) {
        model.addAttribute("roles", rolService.getAllRoles());
        return "rolesLista";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevoRol(Model model) {
        model.addAttribute("rol", new Rol());
        return "rolesCrear";
    }

    @PostMapping("/guardar")
    public String guardarRol(@Valid @ModelAttribute Rol rol,
                             BindingResult result,
                             RedirectAttributes redirectAttributes) {
        if (rolService.findByNombreRol(rol.getNombreRol()).isPresent()) {
            result.rejectValue("nombreRol", "error.rol", "El nombre del rol ya existe.");
        }

        if (result.hasErrors()) {
            return "rolesCrear";
        }

        rolService.saveRol(rol);
        redirectAttributes.addFlashAttribute("successMessage", "Rol creado correctamente.");
        return "redirect:/menuAdmin/roles";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarRol(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        rolService.deleteRol(id);
        redirectAttributes.addFlashAttribute("successMessage", "Rol eliminado.");
        return "redirect:/menuAdmin/roles";
    }
}

