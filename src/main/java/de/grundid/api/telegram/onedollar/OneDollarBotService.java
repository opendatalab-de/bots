package de.grundid.api.telegram.onedollar;

import de.grundid.api.telegram.AbstractBotInitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class OneDollarBotService extends AbstractBotInitService {

	@Autowired
	public OneDollarBotService(@Value("${telegram.onedollarBot.apiKey}") String apiKey) {
		super(apiKey, "https://bots.grundid.de/bot/onedollar");
	}
}
