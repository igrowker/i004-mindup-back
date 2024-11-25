package com.mindup.core.services;

import com.mindup.core.entities.EmailVerification;
import com.mindup.core.entities.PasswordResetToken;
import com.mindup.core.entities.User;
import com.mindup.core.repositories.EmailVerificationRepository;
import com.mindup.core.repositories.PasswordResetTokenRepository;
import com.mindup.core.repositories.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@AllArgsConstructor
@Service
public class EmailVerificationService {

    private final EmailVerificationRepository emailVerificationRepository;
    private final UserRepository userRepository;
    private final JavaMailSender mailSender;
    private final String BASE_URL = "http://localhost:8090"; // tu dominio
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    public EmailVerification findByUser (User user) {
        return emailVerificationRepository.findByUser (user);
    }
//
public void sendVerificationEmail(String email, String token) {
    String verificationLink = "http://localhost:8090/api/core/verify?token=" + token;
    String subject = "Verifica tu correo electrónico";

    // Cuerpo del mensaje en HTML
    String body = "<html>" +
            "<head>" +
            "<style>" +
            "    body { font-family: Arial, sans-serif; margin: 0; padding: 0; background-color: #f4f4f4; }" +
            "    .container { width: 100%; max-width: 600px; margin: auto; background: #ffffff; padding: 20px; border-radius: 8px; box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1); }" +
            "    h1 { color: #333; text-align: center; }" + // Centrar el título
            "    p { color: #555; text-align: center; }" + // Centrar el párrafo
            "    .button { display: inline-block; padding: 8px 16px; font-size: 14px; background-color: #7A5FE7; text-decoration: none; border-radius: 5px; }" + // Estilo del botón sin color de texto
            "    .button-container { text-align: center; margin: 20px 0; }" + // Contenedor para centrar el botón
            "    .footer { margin-top: 20px; font-size: 12px; color: #aaa; text-align: center; }" +
            "    .image-container { text-align: center; margin: 20px 0; }" + // Contenedor para centrar la imagen
            "</style>" +
            "</head>" +
            "<body>" +
            "    <div class='container'>" +
            "        <h1>¡Bienvenido a MindUp!</h1>" +
            "        <p>Por favor, verifica tu correo electrónico haciendo clic en el siguiente enlace:</p>" +
            "        <div class='button-container'>" + // Contenedor para el botón
            "            <a href=\"" + verificationLink + "\" class='button' style='color: #ffffff;'>Verificar correo electrónico</a>" + // Estilo en línea para el color del texto
            "        </div>" +
            "        <p style='text-align: center; color: #ff0000;'>Nota: Este enlace expirará en 24 horas.</p>" +
            "        <div class='image-container'>" + // Contenedor para la imagen
            "            <img src='cid:logoImage' alt='Logo' style='width:150px;height:auto;'/>" + // Imagen en línea
            "        </div>" +
            "        <p>Gracias por unirte a nosotros. Si tienes alguna pregunta, no dudes en contactarnos.</p>" +
            "        <div class='footer'>" +
            "            <p>&copy; " + LocalDate.now().getYear() + " MindUp. Todos los derechos reservados.</p>" +
            "        </div>" +
            "    </div>" +
            "</body>" +
            "</html>";

    MimeMessage message = mailSender.createMimeMessage();
    try {
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(email);
        helper.setSubject(subject);
        helper.setText(body, true);

        helper.addInline("logoImage", new ClassPathResource("/images/MindUpLogo.png"));

        // Guardar la verificación con la fecha de caducidad
        EmailVerification emailVerification = new EmailVerification();
        emailVerification.setUser(userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found")));
        emailVerification.setVerificationToken(token);
        emailVerification.setVerified(true);// se cambia de FALSE > TRUE para modo pruebas
        emailVerification.setExpiryDate(LocalDateTime.now().plusHours(24)); // Establecer la caducidad a 24 horas

        emailVerificationRepository.save(emailVerification);

        mailSender.send(message);
    } catch (MessagingException e) {
        e.printStackTrace();
    }
}

    public boolean verifyEmail(String token) {
        Optional<EmailVerification> verification = emailVerificationRepository.findByVerificationToken(token);
        if (verification.isPresent()) {
            EmailVerification emailVerification = verification.get();
            if (emailVerification.getExpiryDate().isAfter(LocalDateTime.now())) {
                emailVerification.setVerified(true);
                emailVerificationRepository.save(emailVerification);
                return true;
            } else {
                // El token ha caducado
                return false; // O lanzar una excepción según tu lógica
            }
           // emailVerification.setVerified(true);
            //emailVerificationRepository.save(emailVerification);
            //return true;
        }
        return false;
    }

    public void sendPasswordResetEmail(String email, String token) {
        String resetLink = "http://localhost:8090/api/core/resetPW?token=" + token;
        String subject = "Solicitud de Restablecimiento de Contraseña";

        // Cuerpo del mensaje en HTML
        String body = "<html>" +
                "<head>" +
                "   <style>" +
                "       body { font-family: Arial, sans-serif; margin: 0; padding: 0; background-color: #f4f4f4; }" +
                "       .container { width: 100%; max-width: 600px; margin: auto; background: #ffffff; padding: 20px; border-radius: 8px; box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1); }" +
                "       h1 { color: #333; text-align: center; }" +
                "       p { color: #555; text-align: center; }" +
                "       .button { display: inline-block; padding: 8px 16px; font-size: 14px; background-color: #7A5FE7; text-decoration: none; border-radius: 5px; color: #ffffff; }" +
                "       .button-container { text-align: center; margin: 20px 0; }" +
                "       .footer { margin-top: 20px; font-size: 12px; color: #aaa; text-align: center; }" +
                "       .image-container { text-align: center; margin: 20px 0; }" + // Centrar la imagen
                "   </style>" +
                "</head>" +
                "<body>" +
                "    <div class='container'>" +
                "       <h1>Restablecimiento de Contraseña</h1>" +
                "       <p>Para restablecer tu contraseña, haz clic en el siguiente enlace:</p>" +
                "       <div class='button-container'>" + // Contenedor para el botón
                "           <a href=\"" + resetLink + "\" class='button' style='color: #ffffff;'>Restablecer Password</a>" +
                "       </div>" +
                "       <p style='text-align: center; color: #ff0000;'>Nota: Este enlace expirará en 1 hora.</p>" +
                "       <div class='image-container'>" + // Contenedor para la imagen
                "           <img src='cid:logoImage' alt='Logo' style='width:150px;height:auto;'/>" +
                "       </div>" +
                "       <div class='footer'>" +
                "           <p>&copy; " + LocalDate.now().getYear() + " MindUp. Todos los derechos reservados.</p>" +
                "       </div>" +
                "    </div>" +
                "</body>" +
                "</html>";
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(body, true);
            helper.addInline("logoImage", new ClassPathResource("/images/MindUpLogo.png"));

            // Guardar el token de restablecimiento de contraseña con la fecha de caducidad
            PasswordResetToken passwordResetToken = new PasswordResetToken();
            passwordResetToken.setUser (userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User  not found")));
            passwordResetToken.setToken(token);
            passwordResetToken.setExpiryDate(LocalDateTime.now().plusHours(1)); // Establecer la caducidad a 1 hora

            passwordResetTokenRepository.save(passwordResetToken);

            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

}
