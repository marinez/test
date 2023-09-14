package fr.insy2s.sesame.service.impl;

import fr.insy2s.sesame.service.IMailService;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.Mockito.*;

@SpringBootTest
class MailServiceImplTest {

    @Autowired
    private IMailService mailService;

    @MockBean
    private JavaMailSender javaMailSender;

    @Test
    void sendMail() {
        String to = "fbenseddik@insy2s.fr";
        String subject = "subject";
        String content = "content";
        MimeMessage mimeMessage = new MimeMessage((Session) null);
        doReturn(mimeMessage).when(javaMailSender).createMimeMessage();
        mailService.sendMail(to, subject, content);
        verify(javaMailSender, timeout(2000).times(1)).send(mimeMessage);
    }

    @Test
    void sendActivationEmail() {
        String to = "fbenseddik@insy2s.fr";
        String username = "mzimmer";
        String activationKey = "activationKey";
        MimeMessage mimeMessage = new MimeMessage((Session) null);
        doReturn(mimeMessage).when(javaMailSender).createMimeMessage();
        mailService.sendActivationEmail(to, username, activationKey);
        verify(javaMailSender, timeout(2000).times(1)).send(mimeMessage);
    }

    @Test
    void sendNotificationPasswordUpdated() {
        String to = "fbenseddik@insy2s.fr";
        MimeMessage mimeMessage = new MimeMessage((Session) null);
        doReturn(mimeMessage).when(javaMailSender).createMimeMessage();
        mailService.sendNotificationPasswordUpdated(to);
        verify(javaMailSender, timeout(2000).times(1)).send(mimeMessage);
    }

}