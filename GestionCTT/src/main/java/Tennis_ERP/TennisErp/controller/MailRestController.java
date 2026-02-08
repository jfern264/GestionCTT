package Tennis_ERP.TennisErp.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Tennis_ERP.TennisErp.service.EmailService;

@RestController
@RequestMapping("/api/mail") // Esto define la primera parte de la ruta
public class MailRestController {

    @Autowired
    private EmailService emailService;

    // Esto define la segunda parte: /send-massive
    @PostMapping("/send-massive") 
    public ResponseEntity<String> sendMassive(@RequestBody Map<String, String> request) {
        String subject = request.get("subject");
        String content = request.get("content");

        // Llama al servicio asíncrono que ya creamos
        emailService.sendMassiveEmail(subject, content);

        return ResponseEntity.ok("Proceso de envío iniciado");
    }
}