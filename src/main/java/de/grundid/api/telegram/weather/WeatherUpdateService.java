package de.grundid.api.telegram.weather;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.telegram.api.methods.Constants;
import org.telegram.api.methods.SendMessage;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.*;

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
    private Map<String, SensorValue> lastSensorValues = new HashMap<>();

    public WeatherUpdateService() {
        sensors.put("cowo.outside.temperature", "Außentemperatur");
        sensors.put("cowo.inside1.temperature", "Innentemperatur Süd");
        sensors.put("cowo.inside2.temperature", "Innentemperatur Nord");
        sensors.put("cowo.inside2.humidity", "Luftfeuchtigkeit Innen");
        sensors.put("cowo.outside.humidity", "Luftfeuchtigkeit Außen");
    }

    @Scheduled(fixedDelay = 10 * 60 * 1000, initialDelay = 20 * 1000)
    public void updateSensors() {
        forceUpdateSince(lastCheck);
        lastCheck = System.currentTimeMillis();
    }

    public void forceUpdateSince(long lastCheck) {
        String messageContent = getStatus();
        if (messageContent != null) {
            sendMessageToRegisteredUsers(messageContent);
        }
    }

    private void sendMessageToRegisteredUsers(String messageContent) {
        Set<Integer> chatIdsToRemove = new HashSet<>();
        for (Integer chatId : databaseService.getChatIds()) {
            SendMessage sendMessage = new SendMessage();
            sendMessage.enableMarkdown(true);
            sendMessage.setChatId(chatId);
            sendMessage.setText(messageContent);
            try {
                restTemplate
                        .postForEntity(Constants.BASEURL + apiKey + "/" + SendMessage.PATH, sendMessage, String.class);
            }
            catch (Exception e) {
                chatIdsToRemove.add(chatId);
            }
        }
        for (Integer chatId : chatIdsToRemove) {
            databaseService.removeChatId(chatId);
        }
    }

    public String getCurrentValues() {
        StringBuilder stringBuilder = new StringBuilder();
        DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, Locale.GERMANY);
        DecimalFormat decimalFormat = new DecimalFormat("#0.0");
        for (Map.Entry<String, String> entry : sensors.entrySet()) {
            String key = entry.getKey();
            if (key.contains("temperature")) {
                SensorValue sensorValue = lastSensorValues.get(key);
                if (sensorValue != null) {
                    stringBuilder.append("Aktueller Wert von ").append(entry.getValue()).append(" ist ")
                            .append(decimalFormat.format(sensorValue.getValue())).append("°C (vom ")
                            .append(dateFormat.format(new Date(sensorValue.getDate()))).append(")\n");
                }
            }
        }
        if (stringBuilder.length() == 0) {
            stringBuilder.append("Keine aktuellen Werte vorhanden.");
        }
        return stringBuilder.toString();
    }

    public String getStatus() {
        StringBuilder stringBuilder = new StringBuilder();
        DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, Locale.GERMANY);
        for (Map.Entry<String, String> entry : sensors.entrySet()) {
            log.info("loading http://api.grundid.de/sensor?sensorName=" + entry.getKey() + "&size=1");
            PagedResponse pagedResponse = restTemplate
                    .getForObject("http://api.grundid.de/sensor?sensorName=" + entry.getKey() + "&size=1",
                            PagedResponse.class);
            if (pagedResponse != null && pagedResponse.getContent() != null && !pagedResponse.getContent().isEmpty()) {
                SensorValue sensorValue = pagedResponse.getContent().get(0);
                lastSensorValues.put(entry.getKey(), sensorValue);
                long lastValueTimestamp = sensorValue.getDate();
                long diff = System.currentTimeMillis() - lastValueTimestamp;
                long diffInMins = diff / (1000 * 60);
                log.info("Wert " + entry.getKey() + " ist " + diffInMins + " Minuten alt");
                Boolean valueAvailable = currentValueMap.get(entry.getKey());
                boolean currentValueAvailable = valueAvailable == null || valueAvailable;
                if (diffInMins > 15) {
                    if (currentValueAvailable) {
                        stringBuilder.append("Letzter Wert von ").append(entry.getValue()).append(" ist ")
                                .append(diffInMins).append(" Minuten her (vom ")
                                .append(dateFormat.format(new Date(lastValueTimestamp))).append(")\n");
                    }
                    currentValueMap.put(entry.getKey(), false);
                }
                else {
                    if (!currentValueAvailable) {
                        stringBuilder.append("Der Sensor ").append(entry.getValue())
                                .append(" ist wieder online (neuster Wert vom ")
                                .append(dateFormat.format(new Date(lastValueTimestamp))).append(")\n");
                    }
                    currentValueMap.put(entry.getKey(), true);
                }
            }
        }
        return stringBuilder.length() > 0 ? stringBuilder.toString() : null;
    }
}
