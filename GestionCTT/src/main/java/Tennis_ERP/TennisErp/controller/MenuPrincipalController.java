package Tennis_ERP.TennisErp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class MenuPrincipalController {

    @GetMapping("/menu_principal")
    public String menu_principal() {
        return "Menu_principal";  // Retorna el nombre de la vista (Menu_principal.html)
    }
    
    @GetMapping("/menu_admin")
    public String menu_admin() {
        return "menu_admin";  // Retorna el nombre de la vista (menu_admin.html)
    }
}
