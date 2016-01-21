package de.grundid.api.telegram.laboeuf;

import de.grundid.api.telegram.CommandParser;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.api.methods.SendAudio;
import org.telegram.api.methods.SendMessage;
import org.telegram.api.methods.SendPhoto;
import org.telegram.api.objects.Message;
import org.telegram.api.objects.Update;

import java.text.ParseException;

@RestController
public class LaBoeufBotController {

	@RequestMapping(value = "/bot/laboeuf", method = RequestMethod.POST)
	public ResponseEntity<?> post(@RequestBody Update update) {
		Message message = update.getMessage();
		SendMessage sendMessage = new SendMessage();
		sendMessage.setChatId(message.getChatId());
		if(StringUtils.hasText(message.getText())){
			try{
				CommandParser commandParser = new CommandParser(message.getText());
				String command = commandParser.getCommand();
				if("start".equals(command)){
					sendMessage.setText("Just do it. nothing is impossible");
				}
				else if("shia".equals(command)){
					sendMessage.setText("Yesterday you said tomorrow so JUST DO IT. NOTHING IS IMPOSSIBLE");
				}
				else if("stop".equals(command)){
					sendMessage.setText("You did it?");
				}
				else{
					sendMessage.setText("unknown command");
				}
			} catch (ParseException e){
				sendMessage.setText("unknown command");
			}
		} else{
			sendMessage.setText("unknown command. use /stop to quit the bot");
		}
		return ResponseEntity.ok().body(sendMessage);
	}
}
