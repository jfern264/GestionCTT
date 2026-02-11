package Tennis_ERP.TennisErp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import Tennis_ERP.TennisErp.domain.Categoria;
import Tennis_ERP.TennisErp.domain.Usuario;
import Tennis_ERP.TennisErp.service.CategoriaService;
import Tennis_ERP.TennisErp.service.RolService;
import Tennis_ERP.TennisErp.service.UsuarioService;


@Controller
public class MailController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private RolService rolService;

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping("/mail")
    public String mail(Model model) {

        List<Categoria>  categorias = categoriaService.getAllCategorias();
        List<Usuario> usuarios = usuarioService.getAllUsuarios();

         model.addAttribute("categorias", categorias);
         model.addAttribute("Usuarios", usuarios);


        return "/mail/mail";
    }

}
