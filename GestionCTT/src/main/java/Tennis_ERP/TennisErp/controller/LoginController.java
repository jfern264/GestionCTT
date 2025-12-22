package Tennis_ERP.TennisErp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.core.Authentication;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // Cambia el defaultSuccessUrl en SecurityConfig a "/home"
    @GetMapping("/redireccion")
    public String redireccionar(Authentication auth) {
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"));

        if (isAdmin) {
            return "redirect:/menu_admin"; // Ajusta a tu ruta de admin
        } else {
            return "redirect:/menu_principal"; // Esta ruta sí parece existir en tus controladores
        }
    }
}
