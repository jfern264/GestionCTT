package Tennis_ERP.TennisErp.controller;

import Tennis_ERP.TennisErp.service.UsuarioServiceImpl;
import Tennis_ERP.TennisErp.domain.Usuario;
import Tennis_ERP.TennisErp.service.UsuarioService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@Controller
public class TrabajadorController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/trabajadores")
    public String listarTrabajadores(Model model) {
        // Buscamos a los usuarios que tengan el rol de trabajador
        // Asegúrate de que en tu DataInitializer crearas el rol "ROLE_TRABAJADOR"
        List<Usuario> trabajadores = usuarioService.getUsuariosPorRol("ROLE_TRABAJADOR");
        
        model.addAttribute("trabajadores", trabajadores);
        return "trabajadoresLista"; // Debe existir src/main/resources/templates/trabajadoresLista.html
    }
}