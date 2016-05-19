package de.grundid.api.telegram.weather;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.telegram.api.methods.Constants;
import org.telegram.api.methods.SendMessage;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lukas on 19.05.16.
 */
@Service
public class WeatherUpdateService {

    private static Logger log = LoggerFactory.getLogger(WeatherUpdateService.class);
    @Autowired
    private WeatherDatabaseService databaseService;
    @Value("${telegram.cowoHnWeatherBot.apiKey}")
    private String apiKey;
    private RestTemplate restTemplate = new RestTemplate();
    private long lastCheck = System.currentTimeMillis();
    private Map<String, String> sensors = new HashMap<>();
    private Map<String, Boolean> currentValueMap = new HashMap<>();

    public WeatherUpdateService() {
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
        String messageContent = getStatus();
        if (messageContent != null) {
            for (Integer chatId : databaseService.getChatIds()) {
                SendMessage sendMessage = new SendMessage();
                sendMessage.enableMarkdown(true);
                sendMessage.setChatId(chatId);
                sendMessage.setText(messageContent);
                try {
                    ResponseEntity<String> responseEntity = restTemplate
                            .postForEntity(Constants.BASEURL + apiKey + "/" + SendMessage.PATH, sendMessage,
                                    String.class);
                }
                catch (RestClientException e) {
                    throw new IllegalStateException(e);
                }
            }
        }
    }

    public String getStatus() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, String> entry : sensors.entrySet()) {
            log.info("http://api.grundid.de/sensor?sensorName=" + entry.getKey() + "&size=1");
            PagedResponse pagedResponse = restTemplate
                    .getForObject("http://api.grundid.de/sensor?sensorName=" + entry.getKey() + "&size=1",
                            PagedResponse.class);
            if (pagedResponse != null && pagedResponse.getContent() != null && !pagedResponse.getContent().isEmpty()) {
                long diff = (System.currentTimeMillis() - pagedResponse.getContent().get(0).getTimestamp());
                long diffInMins = diff / (1000 * 60);
                log.info("Wert " + entry.getKey() + " ist " + diffInMins + " minuten alt");
                if (diffInMins > 15) {
                    Boolean currentValueAvailable = currentValueMap.get(entry.getKey());
                    if (currentValueAvailable == null || currentValueAvailable) {
                        stringBuilder.append("Letzter Wert von ").append(entry.getValue()).append(" ist ")
                                .append(diffInMins).append(" Minuten her\n");
                    }
                    currentValueMap.put(entry.getKey(), false);
                }
                else {
                    currentValueMap.put(entry.getKey(), true);
                }
            }
        }
        return stringBuilder.length() > 0 ? stringBuilder.toString() : null;
    }
}
