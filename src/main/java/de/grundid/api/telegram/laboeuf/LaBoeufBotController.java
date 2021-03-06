package de.grundid.api.telegram.laboeuf;

import de.grundid.api.telegram.CommandParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

@RestController
public class LaBoeufBotController {

    private static Logger log = LoggerFactory.getLogger(LaBoeufBotController.class);


    @RequestMapping(value = "/bot/laboeuf", method = RequestMethod.POST)
    public ResponseEntity<?> post(@RequestBody Update update) {
        Message message = update.getMessage();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId());
        if (StringUtils.hasText(message.getText())) {
            log.info(getUserName(message) + " - " + message.getText());
            try {
                CommandParser commandParser = new CommandParser(message.getText());
                String command = commandParser.getCommand();
                List<String> params = commandParser.getParams();
                if ("START".equals(command.toUpperCase())) {
                    sendMessage.setText("Just do it.");
                } else if ("SHIA".equals(command.toUpperCase())) {
                    RandomShiaMessage randomShiaMessage = new RandomShiaMessage();
                    sendMessage.setText(randomShiaMessage.getRandomShiaMessage());
                } else if ("ABOUT".equals(command.toUpperCase())) {
                    sendMessage.setText("About:\nThis bot is made to motivate you. The idea came, when I saw Shia " +
                            "Laboeuf's 'Just Do It' motivation speech. https://youtu.be/ZXsQAXx_ao0\n" +
                            "Created By Lukas Himsel (http://lukashimsel.me)\n\n" +
                            "Please rate my bot at: https://telegram.me/storebot?start=LaBoeuf_Bot");
                } else if ("RATE".equals(command.toUpperCase())) {
                    sendMessage.setText("Please rate my bot at: https://telegram.me/storebot?start=LaBoeuf_Bot");
                } else if ("HELP".equals(command.toUpperCase())) {
                    sendMessage.setText("Commands:\n/start Start conversation\n/shia get a motivation message\n/about A message that describes why i made this bot\n/help this message");
                } else if ("ARE".equals(command.toUpperCase()) && params.size() == 2) {
                    if ("YOU".equals(params.get(0).toUpperCase()) && "FAMOUS?".equals(params.get(1).toUpperCase())) {
                        sendMessage.setText("I'm not famous anymore.\nhttp://lukashimsel.me/pics/doit/famous.jpg");
                    } else {
                        sendMessage.setText("unknown command");
                    }
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
