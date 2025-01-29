package Tennis_ERP.TennisErp.controller;

import Tennis_ERP.TennisErp.domain.Categoria;
import Tennis_ERP.TennisErp.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class CategoriaController {

    private final CategoriaService categoriaService;

    @Autowired
    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    // Mostrar todas las categorías
    @GetMapping("/categorias")
    public String mostrarCategorias(Model model) {
        List<Categoria> categorias = categoriaService.listarCategorias();
        model.addAttribute("categorias", categorias);
        return "categorias"; // Vista que muestra todas las categorías
    }

    @GetMapping("/admincategorias")
    public String administrarCategorias(Model model) {
        // Obtener todas las categorías del servicio
        List<Categoria> categorias = categoriaService.listarCategorias();

        // Pasar los datos al modelo
        model.addAttribute("categorias", categorias);

        return "AdminCategorias"; // Nombre de la vista HTML
    }

    // Mostrar formulario para añadir una nueva categoría
    @GetMapping("/addCategoria")
    public String formularioNuevaCategoria(Model model) {
        model.addAttribute("categoria", new Categoria()); // Inicializa una categoría vacía para el formulario
        return "addCategoria"; // Vista para agregar una nueva categoría
    }

    // Guardar una nueva categoría
    @PostMapping("/addCategoria")
    public String guardarCategoria(@ModelAttribute Categoria categoria) {
        categoriaService.guardarCategoria(categoria);
        return "redirect:/admincategorias"; // Redirige a la lista de categorías después de guardar
    }

    // Mostrar el formulario de edición para una categoría
    @GetMapping("/editCategoria/{id}")
    public String mostrarFormularioEdicion(@PathVariable("id") Long id, Model model) {
        Categoria categoria = categoriaService.obtenerCategoriaPorId(id);
        if (categoria == null) {
            throw new IllegalArgumentException("La categoría con ID " + id + " no existe.");
        }
        model.addAttribute("categoria", categoria);
        return "editarCategoria"; // Vista para el formulario de edición
    }

// Procesar la edición de una categoría
    @PostMapping("/editCategoria")
    public String editarCategoria(@ModelAttribute("categoria") Categoria categoria) {
        categoriaService.guardarCategoria(categoria);
        return "redirect:/admincategorias";
    }

// Eliminar una categoría por su ID
    @GetMapping("/deleteCategoria/{id}")
    public String eliminarCategoria(@PathVariable("id") Long id) {
        categoriaService.eliminarCategoria(id);
        return "redirect:/admincategorias";
    }

}
