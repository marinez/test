package fr.insy2s.sesame.service.impl;

import fr.insy2s.sesame.error.exception.MailNotSendException;
import fr.insy2s.sesame.service.IMailService;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

@Service
@Slf4j
@RequiredArgsConstructor
public class MailServiceImpl implements IMailService {

    @Value("${application.front.url}")
    private String frontUrl;

    @Value("${spring.mail.username}")
    private String from;

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;


    /**
     * {@inheritDoc}
     */
    @Override
    @Async
    public void sendMail(String to, String subject, String content) {
        log.debug("Sending email to {} with subject {} and content {}", to, subject, content);
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, StandardCharsets.UTF_8.name());
            message.setTo(to);
            message.setFrom(from);
            message.setSubject(subject);
            message.setText(content, true);
            javaMailSender.send(mimeMessage);
            log.info("Sent email to User '{}'", to);
        } catch (Exception e) {
            throw new MailNotSendException(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
@Async
    public void sendActivationEmail(String to, String username, String activationKey) {
        log.debug("Sending activation email to {}", to);
        Locale locale = Locale.forLanguageTag("fr");
        Context context = new Context(locale);
        context.setVariable("activationKey", activationKey);
        context.setVariable("username", username);
        context.setVariable("baseUrl", frontUrl);
        String content = templateEngine.process("confirmAccount", context);
        String subject = "Activation de votre compte Sesame";
        sendMail(to, subject, content);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Async
    public void sendNotificationPasswordUpdated(String to) {
        log.debug("Sending notification password updated to {}", to);
        Locale locale = Locale.forLanguageTag("fr");
        Context context = new Context(locale);
        String content = templateEngine.process("notificationPasswordUpdated", context);
        String subject = "Modification de votre mot de passe";
        sendMail(to, subject, content);
    }

}
