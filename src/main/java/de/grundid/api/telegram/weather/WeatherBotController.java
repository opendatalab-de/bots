package de.grundid.api.telegram.weather;

import de.grundid.api.telegram.CommandParser;
import de.grundid.api.telegram.laboeuf.RandomShiaMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.List;

/**
 * Created by lukas on 19.05.16.
 */
@RestController
public class WeatherBotController {

    @Autowired
    private WeatherUpdateService weatherUpdateService;

    @RequestMapping(value = "/bot/cowoWeather", method = RequestMethod.POST)
    public ResponseEntity<?> post(@RequestBody Update update) {
        Message message = update.getMessage();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId());
        weatherUpdateService.setChatId(message.getChatId());

        if (StringUtils.hasText(message.getText())) {
            try {
                CommandParser commandParser = new CommandParser(message.getText());
                String command = commandParser.getCommand();

                if ("start".equals(command)) {
                    sendMessage.setText("start service.");
                } else {
                    sendMessage.setText("unknown command");
                }
            } catch (ParseException e) {
                sendMessage.setText("unknown command");
            }
        } else {
            sendMessage.setText("unknown command.");
        }
        return ResponseEntity.ok().body(sendMessage);
    }

    private String getUserName(Message message) {
        if (message.getFrom() != null && message.getFrom().getUserName() != null) {
            return message.getFrom().getUserName();
        } else {
            return "unknown username";
        }
    }
}

