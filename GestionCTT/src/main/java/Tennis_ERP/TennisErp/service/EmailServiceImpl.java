package Tennis_ERP.TennisErp.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import Tennis_ERP.TennisErp.dao.UsuarioDAO;
import Tennis_ERP.TennisErp.domain.Usuario;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UsuarioDAO userRepository;

    /**
     * Envía un correo individual envolviéndolo en una plantilla profesional.
     */
    @Override
    public void sendSingleEmail(String to, String subject, String body) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        
        // Llamamos a la nueva función que da formato
        String formattedBody = wrapHtmlContent(body);
        
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(formattedBody, true); // true indica que es HTML
        
        mailSender.send(message);
    }

    /**
     * Función privada para dar formato "Steel Edition" al mensaje.
     */
    private String wrapHtmlContent(String content) {
        return """
        <html>
        <body style="margin: 0; padding: 0; background-color: #0f172a; font-family: 'Segoe UI', Arial, sans-serif;">
            <table width="100%" border="0" cellspacing="0" cellpadding="0" style="background-color: #0f172a; padding: 20px;">
                <tr>
                    <td align="center">
                        <table width="600" border="0" cellspacing="0" cellpadding="0" style="background-color: #ffffff; border-radius: 24px; overflow: hidden; box-shadow: 0 20px 25px -5px rgba(0, 0, 0, 0.1);">
                            <tr>
                                <td style="background-color: #1e293b; padding: 40px; text-align: center; border-bottom: 4px solid #10b981;">
                                    <h1 style="color: #ffffff; margin: 0; font-size: 24px; text-transform: uppercase; letter-spacing: 3px;">Comunidad CTT</h1>
                                    <p style="color: #10b981; margin: 5px 0 0 0; font-weight: bold; font-size: 12px; letter-spacing: 1px;">STEEL EDITION 2026</p>
                                </td>
                            </tr>
                            <tr>
                                <td style="padding: 40px; background-color: #ffffff; color: #334155; font-size: 16px; line-height: 1.8;">
                                    <div style="margin-bottom: 20px;">
                                        """ + content.replace("\n", "<br>") + """
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td style="padding: 30px; background-color: #f8fafc; text-align: center; border-top: 1px solid #e2e8f0;">
                                    <p style="color: #64748b; font-size: 11px; margin: 0; text-transform: uppercase; letter-spacing: 1px;">
                                        Club de Tenis Terrassa &copy; 2026
                                    </p>
                                    <p style="color: #94a3b8; font-size: 10px; margin: 10px 0 0 0;">
                                        Este es un comunicado oficial enviado a los Usuarios de CTT.
                                    </p>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
        </body>
        </html>
        """;
    }

    @Override
    @Async
    public void sendMassiveEmail(String subject, String body) {
        List<Usuario> users = userRepository.findAll();
        for (Usuario user : users) {
            try {
                // Reutiliza la lógica de envío con formato
                this.sendSingleEmail(user.getEmail(), subject, body);
            } catch (MessagingException e) {
                System.err.println("Error enviando a " + user.getEmail() + ": " + e.getMessage());
            }
        }
    }
}