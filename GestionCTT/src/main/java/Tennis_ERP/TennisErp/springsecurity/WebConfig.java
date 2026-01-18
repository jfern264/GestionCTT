package Tennis_ERP.TennisErp.springsecurity;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Obtenemos la ruta absoluta del proyecto para evitar errores de rutas relativas
        String rootPath = System.getProperty("user.dir");
        String imagesPath = "file:" + rootPath + "/src/main/resources/static/images/";

        // Esto mapea la URL http://localhost:8086/images/nombrefoto.jpg 
        // a la carpeta física en tu disco duro
        registry.addResourceHandler("/images/**")
                .addResourceLocations(imagesPath);
    }
}