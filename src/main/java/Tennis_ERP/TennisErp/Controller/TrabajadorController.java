package Tennis_ERP.TennisErp.controller;

import Tennis_ERP.TennisErp.domain.Trabajador;
import Tennis_ERP.TennisErp.service.TrabajadorService;
import Tennis_ERP.TennisErp.service.RolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
public class TrabajadorController {

    @Autowired
    private TrabajadorService trabajadorService;

    @Autowired
    private RolService rolService;

    // Mostrar la página principal de trabajadores
    @GetMapping("/trabajadores")
    public String mostrarTrabajadores(Model model) {
        List<Trabajador> listaTrabajadores = trabajadorService.getAllTrabajadores();
        model.addAttribute("trabajadores", listaTrabajadores);
        return "Trabajadores"; // Nombre de la plantilla HTML
    }

    @GetMapping("/admintrabajadores")
    public String administrarTrabajadores(Model model) {
        List<Trabajador> listaTrabajadores = trabajadorService.getAllTrabajadores();
        model.addAttribute("trabajadores", listaTrabajadores);
        return "AdminTrabajadores"; // Nombre de la plantilla HTML
    }

    @GetMapping("/admintrabajadores/addTrabajador")
    public String mostrarFormularioAgregarTrabajador(Model model) {
        model.addAttribute("trabajador", new Trabajador());
        model.addAttribute("roles", rolService.findAllRoles()); // Cargar roles para el formulario
        return "addTrabajador";
    }

    @PostMapping("/admintrabajadores/addTrabajador")
    public String agregarTrabajador(@ModelAttribute Trabajador trabajador, RedirectAttributes redirectAttributes) {
        try {
            // Verificar si el DNI ya está registrado
            Optional<Trabajador> trabajadorExistente = trabajadorService.getTrabajadorByDni(trabajador.getDni());
            if (trabajadorExistente.isPresent()) {
                redirectAttributes.addFlashAttribute("error", "El DNI ya está registrado por otro trabajador.");
                return "redirect:/admintrabajadores/addTrabajador";
            }

            trabajadorService.saveTrabajador(trabajador); // Guardar trabajador
            redirectAttributes.addFlashAttribute("success", "Trabajador agregado exitosamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ocurrió un error al agregar el trabajador.");
        }

        return "redirect:/admintrabajadores"; // Redirigir a la lista de trabajadores
    }

    @GetMapping("/admintrabajadores/editTrabajador/{id}")
    public String mostrarFormularioEditarTrabajador(@PathVariable Long id, Model model) {
        if (id == null || id <= 0) {
            model.addAttribute("error", "ID inválido proporcionado");
            return "errorPage"; // Página de error si ID es inválido
        }

        Optional<Trabajador> trabajadorOpt = trabajadorService.getTrabajadorById(id);
        if (!trabajadorOpt.isPresent()) {
            model.addAttribute("error", "Trabajador no encontrado");
            return "errorPage"; // Página de error si el trabajador no se encuentra
        }

        Trabajador trabajador = trabajadorOpt.get();
        model.addAttribute("trabajador", trabajador);
        model.addAttribute("roles", rolService.findAllRoles());
        return "editar_Trabajador"; // Vista para editar trabajador
    }

    private boolean existeTrabajadorConDni(String dni, Long idExcluido) {
        Optional<Trabajador> trabajadorOpt = trabajadorService.getTrabajadorByDni(dni);
        return trabajadorOpt.isPresent() && !trabajadorOpt.get().getId().equals(idExcluido);
    }

    @PostMapping("/admintrabajadores/editTrabajador/{id}")
    public String editarTrabajador(@PathVariable Long id, @ModelAttribute Trabajador trabajador, BindingResult result, RedirectAttributes redirectAttributes, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("roles", rolService.findAllRoles());
            return "editar_Trabajador";
        }

        try {
            Optional<Trabajador> trabajadorExistenteOpt = trabajadorService.getTrabajadorById(id);
            if (!trabajadorExistenteOpt.isPresent()) {
                redirectAttributes.addFlashAttribute("error", "Trabajador no encontrado");
                return "redirect:/admintrabajadores";
            }

            if (existeTrabajadorConDni(trabajador.getDni(), id)) {
                redirectAttributes.addFlashAttribute("error", "El DNI ya está registrado por otro trabajador.");
                return "redirect:/admintrabajadores/editTrabajador/" + id;
            }

            trabajador.setId(id); // Asegurarse de que el ID no cambie
            trabajador.setRol(trabajadorExistenteOpt.get().getRol()); // Mantener el rol actual si no se edita

            trabajadorService.saveTrabajador(trabajador); // Guardar cambios
            redirectAttributes.addFlashAttribute("success", "Trabajador actualizado con éxito");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ocurrió un error inesperado");
        }

        return "redirect:/admintrabajadores";
    }

    @PostMapping("/admintrabajadores/deleteTrabajador/{id}")
    public String eliminarTrabajador(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Optional<Trabajador> trabajadorOpt = trabajadorService.getTrabajadorById(id);
            if (!trabajadorOpt.isPresent()) {
                redirectAttributes.addFlashAttribute("error", "Trabajador no encontrado");
                return "redirect:/admintrabajadores";
            }

            trabajadorService.deleteTrabajador(id);
            redirectAttributes.addFlashAttribute("success", "Trabajador eliminado con éxito");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ocurrió un error al intentar eliminar el trabajador");
        }
        return "redirect:/admintrabajadores";
    }

}
