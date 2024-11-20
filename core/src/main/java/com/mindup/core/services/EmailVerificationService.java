package com.mindup.core.services;

import com.mindup.core.entities.EmailVerification;
import com.mindup.core.entities.User;
import com.mindup.core.repositories.EmailVerificationRepository;
import com.mindup.core.repositories.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class EmailVerificationService {

    private final EmailVerificationRepository emailVerificationRepository;
    private final UserRepository userRepository;
    private final JavaMailSender mailSender;
    private final String BASE_URL = "http://localhost:8090"; // tu dominio

    public EmailVerification findByUser (User user) {
        return emailVerificationRepository.findByUser (user);
    }
//
public void sendVerificationEmail(String email, String token) {
    String verificationLink = "http://localhost:8090/api/core/verify?token=" + token;
    String subject = "Verifica tu correo electrónico";

    // Cuerpo del mensaje en HTML
    String body = "<html>" +
            "<body>" +
            "<h1>¡Bienvenido!</h1>" +
            "<p>Por favor, verifica tu correo electrónico haciendo clic en el siguiente enlace:</p>" +
            "<a href=\"" + verificationLink + "\">Verificar correo electrónico</a>" +
            "<br><br>" +
            "<img src='cid:logoImage' alt='Logo' style='width:100px;height:auto;'/>" + // Imagen en línea
            "<p>Gracias por unirte a nosotros.</p>" +
            "</body>" +
            "</html>";

    MimeMessage message = mailSender.createMimeMessage();
    try {
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(email);
        helper.setSubject(subject);
        helper.setText(body, true);

        helper.addInline("logoImage", new ClassPathResource("/images/MindUpLogo.png"));

        mailSender.send(message);
    } catch (MessagingException e) {
        e.printStackTrace();
    }
}

    public boolean verifyEmail(String token) {
        Optional<EmailVerification> verification = emailVerificationRepository.findByVerificationToken(token);
        if (verification.isPresent()) {
            EmailVerification emailVerification = verification.get();
            emailVerification.setVerified(true);
            emailVerificationRepository.save(emailVerification);
            return true;
        }
        return false;
    }

}
