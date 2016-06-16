package de.grundid.api.telegram.farmbothn;

import de.grundid.api.telegram.AbstractBotInitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class FarmbotBotService extends AbstractBotInitService {

    @Autowired
    public FarmbotBotService(@Value("${telegram.farmbotHn.apiKey}") String apiKey) {
        super(apiKey, "https://bots.grundid.de/bot/farmbotHn");
    }
}
