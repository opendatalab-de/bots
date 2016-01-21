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
							"[Give with PayPal](http://bit.ly/OneDollarBot)");
					sendMessage.enableMarkdown(true);
				}
				else {
					sendMessage.setText(
							"Do you have some money for me?");
				}
			}
			catch (ParseException e) {
				sendMessage.setText(
						"Sorry.");
			}
			return ResponseEntity.ok(sendMessage);
		}
		else {
			sendMessage.setText("Hey. What's up?");
			return ResponseEntity.ok().body(sendMessage);
		}
	}
}
