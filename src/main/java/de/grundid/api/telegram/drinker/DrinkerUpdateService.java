package de.grundid.api.telegram.drinker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.telegram.api.methods.Constants;
import org.telegram.api.methods.SendMessage;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

@Service
public class DrinkerUpdateService {

    private static Logger log = LoggerFactory.getLogger(DrinkerUpdateService.class);
    private NumberFormat priceFormat = DecimalFormat.getCurrencyInstance(Locale.GERMANY);
    private NumberFormat volumeFormat = new DecimalFormat("0.0#'l'");
    @Autowired
    private DrinkerDatabaseService drinkerDatabaseService;
    @Value("${telegram.drinkerBot.apiKey}")
    private String apiKey;
    private RestTemplate restTemplate = new RestTemplate();

    @Scheduled(fixedDelay = 5 * 60 * 1000, initialDelay = 20 * 1000)
    public void updateDrinks() {
        long lastCheck = drinkerDatabaseService.getLastCheck();
        log.info("Checking for new drinks: " + lastCheck);
        PagedResponse pagedResponse = restTemplate
                .getForObject("http://api.grundid.de/drinksmenu/drinks?since={since}&size=50", PagedResponse.class,
                        lastCheck);
        if (pagedResponse != null && pagedResponse.getContent() != null && !pagedResponse.getContent().isEmpty()) {
            for (Integer chatId : drinkerDatabaseService.getChatIds()) {
                SendMessage sendMessage = new SendMessage();
                sendMessage.enableMarkdown(true);
                sendMessage.setChatId(chatId);
                String messageContent = createMessage(pagedResponse.getContent(),
                        drinkerDatabaseService.isAdminChat(chatId));
                log.info("Message with drinks: {}", messageContent);
                sendMessage.setText(messageContent);
                ResponseEntity<String> responseEntity = restTemplate
                        .postForEntity(Constants.BASEURL + apiKey + "/" + SendMessage.PATH, sendMessage, String.class);
                if (responseEntity.getStatusCode().is2xxSuccessful()) {
                    log.info("Send Message OK: " + responseEntity.getBody());
                }
                else {
                    log.error("Error setting hook: " + responseEntity.getBody());
                }
            }
            drinkerDatabaseService.setLastCheck(System.currentTimeMillis());
        }
    }

    public String createMessage(List<DrinkWithLocation> drinkWithLocations, boolean adminChat) {
        StringBuilder sb = new StringBuilder("Neue Drinks seit der letzten Nachricht:\n");
        for (DrinkWithLocation drinkWithLocation : drinkWithLocations) {
            Drink drink = drinkWithLocation.getDrink();
            Location location = drinkWithLocation.getLocation();
            sb.append("[http://grundid.de/drinkerApp/").append(location.getPlaceId()).append("](");
            sb.append(drink.getName()).append(" ").append(priceFormat.format((double)drink.getPrice() / 100));
            if (drink.getVolume() != null) {
                sb.append(" ").append(volumeFormat.format((double)drink.getVolume() / 1000));
            }
            sb.append(")\n");
            sb.append(drink.getCategory()).append("\n");
            if (StringUtils.hasText(drink.getBrand())) {
                sb.append(drink.getBrand()).append("\n");
            }
            if (StringUtils.hasText(drink.getDescription())) {
                sb.append(drink.getDescription()).append("\n");
            }
            sb.append("in Location ").append(location.getName()).append("\n");
            sb.append(location.getAddress()).append("\n");
            sb.append("\n\n");
        }
        return sb.toString();
    }
}
