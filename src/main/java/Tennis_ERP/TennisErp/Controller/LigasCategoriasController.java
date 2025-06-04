/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Tennis_ERP.TennisErp.Controller;

import Tennis_ERP.TennisErp.DAO.RolDAO;
import Tennis_ERP.TennisErp.Service.LigaService;
import Tennis_ERP.TennisErp.Service.UsuarioService;
import Tennis_ERP.TennisErp.Validations.ValidationGroups.*;
import Tennis_ERP.TennisErp.domain.Categoria;
import Tennis_ERP.TennisErp.domain.Liga;
import Tennis_ERP.TennisErp.domain.Rol;
import Tennis_ERP.TennisErp.domain.Usuario;
import Tennis_ERP.TennisErp.service.CategoriaService;
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
public class LigasCategoriasController {

    @Autowired
    private LigaService ligaService;

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping("/menuAdmin/ligas")
    public String listarLigasConCategorias(Model model) {
        List<Liga> ligas = ligaService.getAllLigas(); // Se espera que incluya las categorías
        model.addAttribute("ligas", ligas);
        return "LigasCategoriasListar";
    }

    @GetMapping("/menuAdmin/ligas/nueva")
    public String mostrarFormularioNuevaLiga(Model model) {
        model.addAttribute("liga", new Liga());
        return "ligasCrear";
    }

    @PostMapping("/menuAdmin/ligas/guardar")
    public String guardarLiga(@Valid @ModelAttribute Liga liga, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "ligasCrear";
        }

        ligaService.saveLiga(liga);
        redirectAttributes.addFlashAttribute("successMessage", "Liga creada correctamente.");
        return "redirect:/menuAdmin/ligas";
    }

    @GetMapping("/menuAdmin/ligas/editar/{id}")
    public String mostrarFormularioEditarLiga(@PathVariable Long id, Model model) {
        Liga liga = ligaService.getLigaById(id).orElseThrow(()
                -> new IllegalArgumentException("Liga no encontrada con ID: " + id));
        model.addAttribute("liga", liga);
        return "ligasEditar"; // Vista que tienes que crear
    }

    @PostMapping("/menuAdmin/ligas/actualizar/{id}")
    public String actualizarLiga(@PathVariable Long id,
            @Valid @ModelAttribute Liga liga,
            BindingResult result,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "ligasEditar";
        }

        Liga ligaOriginal = ligaService.getLigaById(id).orElseThrow();

        // Solo actualizamos el nombre, pero conservamos las categorías ya asignadas
        ligaOriginal.setNombre(liga.getNombre());

        // No tocar lista de categorías (Hibernate la detecta como borrada si no se mantiene la misma instancia)
        ligaService.saveLiga(ligaOriginal);

        redirectAttributes.addFlashAttribute("successMessage", "Liga actualizada correctamente.");
        return "redirect:/menuAdmin/ligas";
    }

    @GetMapping("/menuAdmin/ligas/eliminar/{id}")
    public String eliminarLiga(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        ligaService.deleteLiga(id);
        redirectAttributes.addFlashAttribute("successMessage", "Liga eliminada correctamente.");
        return "redirect:/menuAdmin/ligas";
    }

    @GetMapping("/menuAdmin/ligas/categorias/nueva/{ligaId}")
    public String mostrarFormularioNuevaCategoria(@PathVariable Long ligaId, Model model) {
        Categoria categoria = new Categoria();
        Liga liga = ligaService.getLigaById(ligaId).orElseThrow();
        categoria.setLiga(liga);
        model.addAttribute("categoria", categoria);
        return "categoriasCrear";
    }

    @PostMapping("/menuAdmin/ligas/categorias/guardar")
    public String guardarCategoria(@Valid @ModelAttribute Categoria categoria, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "categoriasCrear";
        }

        categoriaService.saveCategoria(categoria);
        redirectAttributes.addFlashAttribute("successMessage", "Categoría creada correctamente.");
        return "redirect:/menuAdmin/ligas";
    }

    @GetMapping("/categorias/eliminar/{id}")
    public String eliminarCategoria(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Categoria categoria = categoriaService.getCategoriaById(id).orElseThrow();
        Long ligaId = categoria.getLiga().getId();

        categoriaService.deleteCategoria(id);
        redirectAttributes.addFlashAttribute("successMessage", "Categoría eliminada correctamente.");

        return "redirect:/menuAdmin/ligas"; // o redirige a /menuAdmin/ligas/categorias/{ligaId} si prefieres
    }

    @GetMapping("/categorias/editar/{id}")
    public String mostrarFormularioEditarCategoria(@PathVariable Long id, Model model) {
        Categoria categoria = categoriaService.getCategoriaById(id).orElseThrow(()
                -> new IllegalArgumentException("Categoría no encontrada con ID: " + id));
        model.addAttribute("categoria", categoria);
        return "categoriasEditar"; // Asegúrate de tener esta vista creada
    }

    @PostMapping("/menuAdmin/ligas/categorias/actualizar/{id}")
    public String actualizarCategoria(@PathVariable Long id,
            @Valid @ModelAttribute Categoria categoria,
            BindingResult result,
            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "categoriasEditar";
        }

        // Recupera la existente para conservar relaciones
        Categoria original = categoriaService.getCategoriaById(id).orElseThrow();
        categoria.setId(id);
        categoria.setUsuarioCategorias(original.getUsuarioCategorias()); // <- aquí la clave

        categoriaService.saveCategoria(categoria);
        redirectAttributes.addFlashAttribute("successMessage", "Categoría actualizada correctamente.");
        return "redirect:/menuAdmin/ligas";
    }
}
