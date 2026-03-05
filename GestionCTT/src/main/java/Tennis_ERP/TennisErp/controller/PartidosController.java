package Tennis_ERP.TennisErp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import ch.qos.logback.core.model.Model;
import jakarta.servlet.http.HttpSession;

@Controller
public class PartidosController {

        
    @GetMapping("/partidos")
    public String listarPartidos(Model model, HttpSession session) {
        return "partidos/lista";
    }
}
