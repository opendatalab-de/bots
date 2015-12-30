package de.grundid.api.telegram.drinker;

import de.grundid.api.telegram.AbstractBotInitService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DrinkerBotService extends AbstractBotInitService {

    public DrinkerBotService(@Value("${telegram.drinkerBot.apiKey}") String apiKey) {
        super(apiKey, "https://bots.grundid.de/bot/drinker");
    }
}
