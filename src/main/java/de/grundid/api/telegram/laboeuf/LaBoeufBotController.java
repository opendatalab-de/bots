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
import java.util.List;

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
				if("START".equals(command.toUpperCase())){
					sendMessage.setText("Just do it.");
				}
				else if("SHIA".equals(command.toUpperCase())){
					RandomShiaMessage randomShiaMessage = new RandomShiaMessage();
					sendMessage.setText(randomShiaMessage.getRandomShiaMessage());
				}
				else if("ABOUT".equals(command.toUpperCase())){
					sendMessage.setText("About:\nThis bot is made to motivate you. The idea came, when I saw Shia Laboeuf's 'Just Do It' motivation speech. https://youtu.be/ZXsQAXx_ao0\nCreated By Lukas Himsel (http://lukashimsel.me)");
				}
				else if("HELP".equals(command.toUpperCase())){
					sendMessage.setText("Commands:\n/start Start conversation\n/shia get a motivation message\n/about A message that describes why i made this bot\n/help this message");
				}
				else if("AREYOUFAMOUS".equals(command.toUpperCase())){
					sendMessage.setText("No more");
				}
				else if("ARE".equals(command.toUpperCase())){
					List<String> params= commandParser.getParams();
					if("YOU".equals(params.get(0).toUpperCase()) && "FAMOUS".equals(params.get(1).toUpperCase())){
						sendMessage.setText("I'm not famous anymore.\nhttp://lukashimsel.me/pics/doit/famous.jpg");
					}
				}
				else{
					sendMessage.setText("unknown command");
				}
			} catch (ParseException e){
				sendMessage.setText("unknown command");
			}
		} else{
			sendMessage.setText("unknown command.");
		}

		return ResponseEntity.ok().body(sendMessage);
	}
}
