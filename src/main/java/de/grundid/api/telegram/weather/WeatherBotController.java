package de.grundid.api.telegram.weather;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.grundid.api.telegram.CommandParser;
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

/**
 * Created by lukas on 19.05.16.
 */
@RestController
public class WeatherBotController {

    private static Logger log = LoggerFactory.getLogger(WeatherBotController.class);
    @Autowired
    private WeatherUpdateService weatherUpdateService;
    @Autowired
    private WeatherDatabaseService databaseService;

    @RequestMapping(value = "/bot/cowoWeather", method = RequestMethod.POST)
    public ResponseEntity<?> post(@RequestBody Update update) {
        try {
            log.info(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(update));
        }
        catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        Message message = update.getMessage();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId());
        if (StringUtils.hasText(message.getText())) {
            try {
                CommandParser commandParser = new CommandParser(message.getText());
                String command = commandParser.getCommand();
                if ("start".equals(command)) {
                    sendMessage.setText("start service.");
                }
                if ("start".equals(command)) {
                    databaseService.addChatId(message.getChatId());
                    sendMessage.setText("Ok, habe mir diesen Chat gemerkt.");
                }
                else if ("stop".equals(command)) {
                    databaseService.removeChatId(message.getChatId());
                    sendMessage.setText("Alles klar. Keine weiteren Updates.");
                }
                else {
                    sendMessage.setText("unknown command");
                }
            }
            catch (ParseException e) {
                sendMessage.setText("unknown command");
            }
        }
        else {
            sendMessage.setText("unknown command.");
        }
        return ResponseEntity.ok().body(sendMessage);
    }

    private String getUserName(Message message) {
        if (message.getFrom() != null && message.getFrom().getUserName() != null) {
            return message.getFrom().getUserName();
        }
        else {
            return "unknown username";
        }
    }
}

