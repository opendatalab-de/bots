package de.grundid.api.telegram.recycling;

import de.grundid.api.telegram.AbstractBotInitService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class BotService extends AbstractBotInitService {

    public BotService(@Value("${telegram.recyclingBot.apiKey}") String apiKey) {
        super(apiKey, "https://bots.grundid.de/bot/recycling");
    }
}
