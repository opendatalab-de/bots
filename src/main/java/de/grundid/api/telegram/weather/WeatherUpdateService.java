package de.grundid.api.telegram.weather;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.telegram.api.methods.Constants;
import org.telegram.api.methods.SendMessage;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;

/**
 * Created by lukas on 19.05.16.
 */
@Service
public class WeatherUpdateService {

    @Value("${telegram.cowoHnWeatherBot.apiKey}")
    private String apiKey;
    private RestTemplate restTemplate = new RestTemplate();
    private long lastCheck = System.currentTimeMillis();
    private Integer chatId;
    private Map<String, String> sensors = new HashMap<>();
    public WeatherUpdateService(){
        sensors.put("cowo.outside.temperature", "Außentemperatur");
        sensors.put("cowo.inside1.temperature", "Innentemperatur Süd");
        sensors.put("cowo.inside2.temperature", "Innentemperatur Nord");
        sensors.put("cowo.inside2.humidity", "Luftfeuchtigkeit Innen");
        sensors.put("cowo.outside.humidity", "Luftfeuchtigkeit Außen");
    }

    @Scheduled(fixedDelay = 5 * 60 * 1000, initialDelay = 20 * 1000)
    public void updateDrinks() {
        forceUpdateSince(lastCheck);
        lastCheck = System.currentTimeMillis();
    }

    public void forceUpdateSince(long lastCheck) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        String messageContent = getStatus();
        sendMessage.setText(messageContent);
        try {
            ResponseEntity<String> responseEntity = restTemplate
                    .postForEntity(Constants.BASEURL + apiKey + "/" + SendMessage.PATH, sendMessage, String.class);
        }
        catch (RestClientException e) {
            throw new IllegalStateException(e);
        }
    }

    public String getStatus() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, String> entry : sensors.entrySet()) {
            de.grundid.api.telegram.weather.PagedResponse pagedResponse = restTemplate
                    .getForObject("http://api.grundid.de/sensor?sensorName=" + entry.getKey() + "&size=1", de.grundid.api.telegram.weather.PagedResponse.class, lastCheck);

            if (pagedResponse != null && pagedResponse.getContent() != null && !pagedResponse.getContent().isEmpty()) {
                long diff = (System.currentTimeMillis() - pagedResponse.getContent().get(0).getTimestamp()) / 60 / 1000;
                stringBuilder.append("Letzter Wert von " + entry.getValue() + " ist " + diff + " Minuten her\n");
            }
        }
        return stringBuilder.toString();
    }

    public void setChatId(Integer chatId) {
        this.chatId = chatId;
    }
}
