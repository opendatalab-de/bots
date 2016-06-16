package de.grundid.api.telegram.farmbothn;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;
import org.telegram.api.methods.Constants;
import org.telegram.api.methods.SendMessage;

import java.util.HashSet;
import java.util.Set;

public class FarmbotUpdateService {

    @Autowired
    private FarmbotDatabaseService databaseService;

    @Value("${telegram.farmbotHn.apiKey}")
    private String apiKey;

    private RestTemplate restTemplate = new RestTemplate();

    @Scheduled(fixedDelay = 6 * 60 * 1000)
    public void sendUpdateMessage() {
        Set<Integer> chatIdsToRemove = new HashSet<>();
        for (Integer chatID : databaseService.getChatIds()) {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatID);
            sendMessage.setText("TEST Update");

            try {
                restTemplate.postForEntity(Constants.BASEURL + apiKey + "/" + SendMessage.PATH, sendMessage, String.class);
            } catch (Exception e) {
                chatIdsToRemove.add(chatID);
            }

        }

        for (Integer chatID : chatIdsToRemove) {
            databaseService.removeChatId(chatID);
        }


    }
}
