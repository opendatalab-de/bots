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
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by lukas on 19.05.16.
 */
@Service
public class WeatherUpdateService {

    private NumberFormat priceFormat = DecimalFormat.getCurrencyInstance(Locale.GERMANY);
    private NumberFormat volumeFormat = new DecimalFormat("0.0#'l'");
    private DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG);

    @Value("${telegram.drinkerBot.apiKey}")
    private String apiKey;
    private RestTemplate restTemplate = new RestTemplate();
    private long lastCheck = System.currentTimeMillis();
    private Integer chatId;

    @Scheduled(fixedDelay = 5 * 60 * 1000, initialDelay = 20 * 1000)
    public void updateDrinks() {
        forceUpdateSince(lastCheck);
        lastCheck = System.currentTimeMillis();
    }

    public void forceUpdateSince(long lastCheck) {
        de.grundid.api.telegram.weather.PagedResponse pagedResponse = restTemplate
                .getForObject("http://api.grundid.de/sensor?sensorName=cowo.outside.temperature&size=1", de.grundid.api.telegram.weather.PagedResponse.class,
                        lastCheck);
        if (pagedResponse != null && pagedResponse.getContent() != null && !pagedResponse.getContent().isEmpty()) {

            long last = System.currentTimeMillis() - pagedResponse.getContent().get(0).getTimestamp();
            if(last > (5 * 60 * 1000)){
                SendMessage sendMessage = new SendMessage();
                sendMessage.enableMarkdown(true);
                sendMessage.setChatId(chatId);
                String messageContent = "Der letzte Wert ist mehr als 5 Minuten her";
                sendMessage.setText(messageContent);
                try {
                    ResponseEntity<String> responseEntity = restTemplate
                            .postForEntity(Constants.BASEURL + apiKey + "/" + SendMessage.PATH, sendMessage, String.class);
                }
                catch (RestClientException e) {
                    throw new IllegalStateException(e);
                }
            }

        }
    }

    public void setChatId(Integer chatId) {
        this.chatId = chatId;
    }
}
