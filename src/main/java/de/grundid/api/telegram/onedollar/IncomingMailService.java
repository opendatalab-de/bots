package de.grundid.api.telegram.onedollar;

import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class IncomingMailService {

    @ServiceActivator(inputChannel = "mailChannel")
    public void newMessage(MimeMessage mailMessage) {
        try {
            System.out.println("new message " + mailMessage.getSubject());
        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }
}
