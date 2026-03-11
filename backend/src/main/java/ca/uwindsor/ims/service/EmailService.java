package ca.uwindsor.ims.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Sends registration confirmation emails to newly created students.
 * Email sending is best-effort: failures are logged but do not abort registration.
 * Set spring.mail.enabled=true and configure SMTP properties to activate.
 */
@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;

    @Value("${spring.mail.enabled:false}")
    private boolean enabled;

    @Value("${spring.mail.username:noreply@ims.local}")
    private String fromAddress;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendRegistrationEmail(String toEmail, String username, String password, Integer studentId) {
        if (!enabled) {
            log.info("Email disabled — skipping registration email to {}", toEmail);
            return;
        }
        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setFrom(fromAddress);
            msg.setTo(toEmail);
            msg.setSubject("IMS Registration — Your Account Details");
            msg.setText("""
                    Welcome to the Internship Management System!

                    Your account has been created. Please log in with the following credentials:

                      Username : %s
                      Password : %s
                      Student ID: %d

                    Please change your password after your first login.

                    Regards,
                    IMS Administration
                    """.formatted(username, password, studentId));
            mailSender.send(msg);
            log.info("Registration email sent to {}", toEmail);
        } catch (Exception ex) {
            log.warn("Failed to send registration email to {}: {}", toEmail, ex.getMessage());
        }
    }
}
