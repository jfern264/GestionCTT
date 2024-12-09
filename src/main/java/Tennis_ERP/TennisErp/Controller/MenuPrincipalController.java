/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/SpringFramework/Controller.java to edit this template
 */
package Tennis_ERP.TennisErp.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author Adria
 */
@Controller
public class MenuPrincipalController {
    
    @GetMapping("/menu_principal")
    public String menu_principal() {
        return "Menu_principal";  // Retorna el nombre de la vista (Menu_principal.html)
    }
    
    
}
