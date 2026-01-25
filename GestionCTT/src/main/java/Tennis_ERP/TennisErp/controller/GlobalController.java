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
        String username = principal.getName();

        // 2. Buscamos los datos completos del socio en la BD
        // Usamos .orElseThrow() para extraer el Usuario o lanzar un error si no existe
        Usuario usuario = usuarioService.findByNombreUsuario(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + username));

        // 3. Ahora pasamos el objeto Usuario (ya no es un Optional)
        model.addAttribute("usuario", usuario);

    }

}