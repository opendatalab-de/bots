package de.grundid.api.telegram;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.telegram.api.methods.Constants;
import org.telegram.api.methods.SetWebhook;

import javax.annotation.PostConstruct;

@Service
public class BotService {

    private static Logger log = LoggerFactory.getLogger(BotService.class);
    private RestTemplate restTemplate = new RestTemplate();
    @Value("${telegram.recyclingBot.apiKey}")
    private String apiKey;

    @PostConstruct
    public void setWebhook() {
        log.info("Init Telegram Bot Webhook");
        SetWebhook setWebhook = new SetWebhook();
        setWebhook.setUrl("https://bots.grundid.de/bot/recycling");
        ResponseEntity<String> responseEntity = restTemplate
                .postForEntity(Constants.BASEURL + apiKey + "/" + SetWebhook.PATH, setWebhook, String.class);
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            log.info("Set Hook ok: " + responseEntity.getBody());
        }
        else {
            log.error("Error setting hook: " + responseEntity.getBody());
        }
    }
}
