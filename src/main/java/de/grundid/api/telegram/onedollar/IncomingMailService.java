package de.grundid.api.telegram.onedollar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestOperations;
import org.telegram.api.methods.Constants;
import org.telegram.api.methods.SendMessage;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class IncomingMailService {

    @Value("${telegram.onedollarBot.apiKey}")
    private String apiKey;

    private static Logger log = LoggerFactory.getLogger(IncomingMailService.class);

    @Autowired
    private RestOperations restOperations;
    @Autowired
    private OneDollarDatabaseService oneDollarDatabaseService;

    @ServiceActivator(inputChannel = "mailChannel")
    public void newMessage(MimeMessage mailMessage) {
        try {
            System.out.println("new message " + mailMessage.getSubject());

            for (UserAndChatId userAndChatId : oneDollarDatabaseService.chatIds()) {
                sendMessage(userAndChatId.getChatId(), "new message " + mailMessage.getSubject());
            }
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(Integer chatId, String messageContent) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setText(messageContent);
        try {
            ResponseEntity<String> responseEntity = restOperations
                    .postForEntity(Constants.BASEURL + apiKey + "/" + SendMessage.PATH, sendMessage,
                            String.class);
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                log.info("Send Message OK: " + responseEntity.getBody());
            } else {
                log.error("Error setting hook: " + responseEntity.getBody());
            }
        } catch (RestClientException e) {
            oneDollarDatabaseService.removeChatId(chatId);
        }
    }
}
