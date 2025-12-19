package Tennis_ERP.TennisErp.Controller;

import Tennis_ERP.TennisErp.Service.LigaService;
import Tennis_ERP.TennisErp.Service.UsuarioCategoriaService;
import Tennis_ERP.TennisErp.Service.UsuarioService;
import Tennis_ERP.TennisErp.Domain.Categoria;
import Tennis_ERP.TennisErp.Domain.Liga;
import Tennis_ERP.TennisErp.Domain.Usuario;
import Tennis_ERP.TennisErp.Resources.FranjaEdad;
import Tennis_ERP.TennisErp.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class EquipoController {

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private LigaService ligaService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioCategoriaService usuarioCategoriaService;

    @GetMapping("/menuAdmin/equipos")
    public String mostrarEquipos(Model model) {
        List<Liga> ligas = ligaService.getAllWithCategoriasAndUsuarios();
        model.addAttribute("ligas", ligas);
        return "equiposListar";
    }

    @GetMapping("/menuAdmin/equipos/{categoriaId}/editar")
    public String editarEquipo(@PathVariable Long categoriaId, Model model) {
        Categoria categoria = categoriaService.getCategoriaById(categoriaId)
                .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada"));
        model.addAttribute("categoria", categoria);
        return "equiposEditar";
    }

    @GetMapping("/menuAdmin/equipos/{categoriaId}/añadir-jugador")
    public String mostrarFormularioAñadirJugador(@PathVariable Long categoriaId, Model model) {
        Categoria categoria = categoriaService.getCategoriaById(categoriaId)
                .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada"));

        // 1) Lista de jugadores que aún no están en esta categoría
        List<Usuario> jugadoresDisponibles = usuarioService.getJugadoresDisponibles(categoriaId);

        // 2) Todas las franjas de edad (desde el enum)
        FranjaEdad[] franjas = FranjaEdad.values();

        model.addAttribute("categoria", categoria);
        model.addAttribute("jugadoresDisponibles", jugadoresDisponibles);

        // ¡Éste es el atributo que faltaba!
        model.addAttribute("franjasEdad", franjas);

        return "equiposAñadirJugador";
    }

    @PostMapping("/menuAdmin/equipos/{categoriaId}/añadir-jugador")
    public String asignarJugadorACategoria(@PathVariable Long categoriaId,
            @RequestParam Long usuarioId,
            RedirectAttributes redirect) {
        usuarioCategoriaService.asociarJugadorACategoria(usuarioId, categoriaId);
        redirect.addFlashAttribute("successMessage", "Jugador añadido correctamente.");
        return "redirect:/menuAdmin/equipos/" + categoriaId + "/editar";
    }

    @GetMapping("/menuAdmin/equipos/{categoriaId}/activar/{ucId}")
    public String activarJugador(@PathVariable Long categoriaId,
            @PathVariable Long ucId,
            RedirectAttributes redirect) {
        usuarioCategoriaService.marcarActivo(ucId);
        redirect.addFlashAttribute("successMessage", "Jugador marcado como activo.");
        return "redirect:/menuAdmin/equipos/" + categoriaId + "/editar";
    }

    @GetMapping("/menuAdmin/equipos/{categoriaId}/suplente/{ucId}")
    public String ponerSuplente(@PathVariable Long categoriaId,
            @PathVariable Long ucId,
            RedirectAttributes redirect) {
        usuarioCategoriaService.marcarSuplente(ucId);
        redirect.addFlashAttribute("successMessage", "Jugador marcado como suplente.");
        return "redirect:/menuAdmin/equipos/" + categoriaId + "/editar";
    }

    @GetMapping("/menuAdmin/equipos/{categoriaId}/eliminar-jugador/{ucId}")
    public String eliminarJugador(@PathVariable Long categoriaId,
            @PathVariable Long ucId,
            RedirectAttributes redirect) {
        usuarioCategoriaService.desasociarJugadorDeCategoria(ucId);
        redirect.addFlashAttribute("successMessage", "Jugador eliminado del equipo.");
        return "redirect:/menuAdmin/equipos/" + categoriaId + "/editar";
    }
}
