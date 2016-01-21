package de.grundid.api.telegram.laboeuf;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.api.methods.SendMessage;
import org.telegram.api.objects.Message;
import org.telegram.api.objects.Update;

@RestController
public class LaBoeufBotController {

	@RequestMapping(value = "/bot/laboeuf", method = RequestMethod.POST)
	public ResponseEntity<?> post(@RequestBody Update update) {
		Message message = update.getMessage();
		SendMessage sendMessage = new SendMessage();
		sendMessage.setChatId(message.getChatId());
		sendMessage.setText("Hey. Are you motivated?");
		return ResponseEntity.ok().body(sendMessage);
	}
}
