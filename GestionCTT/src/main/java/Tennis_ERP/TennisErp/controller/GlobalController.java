package Tennis_ERP.TennisErp.controller;

import jakarta.servlet.http.HttpSession;

import java.security.Principal;

import org.aspectj.internal.lang.annotation.ajcDeclareAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import Tennis_ERP.TennisErp.domain.Usuario;
import Tennis_ERP.TennisErp.service.UsuarioService;

@ControllerAdvice
public class GlobalController {

    @Autowired
    private UsuarioService usuarioService;

    @ModelAttribute
    public void addAttributes(Model model, Principal principal) {
        // 1. Verificamos SI existe el principal antes de hacer nada
        if (principal != null) {
            String username = principal.getName();

            // 2. Buscamos los datos completos del socio en la BD
            // Es buena práctica manejar el caso de que no exista en la BD sin romper la app
            Usuario usuario = usuarioService.findByNombreUsuario(username).orElse(null);

            if (usuario != null) {
                model.addAttribute("usuario", usuario);
            }
        }
        // Si principal es null, el método simplemente no hace nada y la página carga
        // normal
    }

}