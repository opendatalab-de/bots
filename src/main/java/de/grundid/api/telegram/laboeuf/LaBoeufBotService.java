package de.grundid.api.telegram.laboeuf;

import de.grundid.api.telegram.AbstractBotInitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class LaBoeufBotService extends AbstractBotInitService {

	@Autowired
	public LaBoeufBotService(@Value("${telegram.laboeufBot.apiKey}") String apiKey) {
		super(apiKey, "https://bots.grundid.de/bot/laboeuf");
	}
}
