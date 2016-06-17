package de.grundid.api.telegram.farmbothn;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.telegram.api.methods.Constants;
import org.telegram.api.methods.SendMessage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

@Service
public class FarmbotUpdateService {

    @Autowired
    private FarmbotDatabaseService databaseService;

    @Value("${telegram.farmbotHn.apiKey}")
    private String apiKey;

    private Double lastPercent;
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME;
    private LocalDateTime localDateTime = getLocalDateTime();

    private RestTemplate restTemplate = new RestTemplate();

    @Scheduled(fixedDelay = 60 * 1000)
    public void sendUpdateMessage() {
        Set<Integer> chatIdsToRemove = new HashSet<>();
        for (Integer chatID : databaseService.getChatIds()) {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatID);
            if (lastPercent != null) {
                long diffInSeconds = java.time.Duration.between(localDateTime, LocalDateTime.now()).getSeconds();
                if (LocalDateTime.now().isAfter(localDateTime) && diffInSeconds < 600) {
                    sendMessage.setText("Aktuelle Feuchtigkeit:" + lastPercent.toString());
                } else {
                    sendMessage.setText("Achtung! Letzte Feuchtigkeitswerte vor mehr als 10 Minuten, Verbindung zum Pi gescheitert");
                }
            } else {
                sendMessage.setText("Noch keine Prozentwerte");
            }

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

    public void setLastPercent(double lastPercent) {
        this.lastPercent = lastPercent;
    }

    public Double getLastPercent() {
        return lastPercent;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(String time) {
        localDateTime = (LocalDateTime) dateTimeFormatter.parse(time);
    }
}
