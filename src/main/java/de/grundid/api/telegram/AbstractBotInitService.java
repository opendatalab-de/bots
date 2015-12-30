package de.grundid.api.telegram;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.telegram.api.methods.Constants;
import org.telegram.api.methods.SetWebhook;

import javax.annotation.PostConstruct;

public class AbstractBotInitService {

    private static Logger log = LoggerFactory.getLogger(AbstractBotInitService.class);
    private String apiKey;
    private String botUrl;
    private RestTemplate restTemplate = new RestTemplate();

    public AbstractBotInitService(String apiKey, String botUrl) {
        this.apiKey = apiKey;
        this.botUrl = botUrl;
    }

    @PostConstruct
    public void setWebhook() {
        if (apiKey != null) {
            log.info("Init Telegram Bot Webhook");
            SetWebhook setWebhook = new SetWebhook();
            setWebhook.setUrl(botUrl);
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
}
