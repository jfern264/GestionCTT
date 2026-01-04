package Tennis_ERP.TennisErp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MenuController {

    @GetMapping("/menu_principal")
    public String menu_principal() {
        return "acceso/menu_principal";
    }

    @GetMapping("/menu_admin")
    public String menu_admin() {
        return "acceso/menu_admin";
    }
}