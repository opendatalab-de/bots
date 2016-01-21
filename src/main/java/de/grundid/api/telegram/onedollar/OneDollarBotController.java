package de.grundid.api.telegram.onedollar;

import de.grundid.api.telegram.CommandParser;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.api.methods.SendMessage;
import org.telegram.api.objects.Message;
import org.telegram.api.objects.Update;

import java.text.ParseException;

@RestController
public class OneDollarBotController {

	@RequestMapping(value = "/bot/onedollar", method = RequestMethod.POST)
	public ResponseEntity<?> post(@RequestBody Update update) {
		Message message = update.getMessage();
		SendMessage sendMessage = new SendMessage();
		sendMessage.setChatId(message.getChatId());
		if (StringUtils.hasText(message.getText())) {
			try {
				CommandParser commandParser = new CommandParser(message.getText());
				String command = commandParser.getCommand();
				if ("give".equals(command)) {
					sendMessage.setText(
							"[Give with PayPal](https://www.paypal.com/cgi-bin/webscr?cmd=_xclick&business=loki%2de%40web%2ede&lc=US&item_name=OneDollarBot&amount=1%2e00&currency_code=USD&button_subtype=services&no_note=0&tax_rate=0%2e000&shipping=0%2e00&bn=PP%2dBuyNowBF%3abtn_buynowCC_LG%2egif%3aNonHostedGuest)");
					sendMessage.enableMarkdown(true);
				}
			}
			catch (ParseException e) {
				sendMessage.setText(
						"Sorry.");
			}
			return ResponseEntity.ok(sendMessage);
		}
		else {
			return ResponseEntity.ok().body(null);
		}
	}
}
