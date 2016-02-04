package de.grundid.api.telegram.onedollar;

import de.grundid.api.telegram.CommandParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.telegram.api.methods.SendMessage;
import org.telegram.api.objects.Message;
import org.telegram.api.objects.ReplyKeyboardMarkup;
import org.telegram.api.objects.Update;

import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;

@Controller
public class OneDollarBotController {

    private static final char[] MONEY_BAG = Character.toChars(0x1F4B0);
    private static final String GIVE_ONE_DOLLAR =
            "I'll give you one dollar " + MONEY_BAG[0] + MONEY_BAG[1];

    @Autowired
    private OneDollarDatabaseService oneDollarDatabaseService;

    @Autowired
    private ImageUpdateService imageUpdateService;



    @RequestMapping(value = "/bot/onedollar", method = RequestMethod.POST)
    public ResponseEntity<?> post(@RequestBody Update update) throws IOException {
        Message message = update.getMessage();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId());

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setOneTimeKeyboad(true);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup
                .setKeyboard(Arrays.asList(Arrays.asList(GIVE_ONE_DOLLAR)));
        if (StringUtils.hasText(message.getText())) {
            if (CommandParser.isCommand(message.getText())) {
                try {
                    CommandParser commandParser = new CommandParser(message.getText());
                    String command = commandParser.getCommand();
                    if ("give".equals(command)) {
                        sendMessage.setText(
                                "[Give one dollar with PayPal](http://bit.ly/OneDollarBot)");
                        sendMessage.enableMarkdown(true);
                        imageUpdateService.setSendImageChatid(message.getChatId());
                    } else {
                        sendMessage.setText(
                                "Try /give");
                        sendMessage.setReplayMarkup(keyboardMarkup);
                    }
                } catch (ParseException e) {
                    sendMessage.setText(
                            "Do you have some money for me?");
                    sendMessage.setReplayMarkup(keyboardMarkup);
                }
            } else {
                if (GIVE_ONE_DOLLAR.equals(message.getText())) {
                    sendMessage.setText(
                            "[Give one dollar with PayPal](http://bit.ly/OneDollarBot)");
                    sendMessage.enableMarkdown(true);
                    oneDollarDatabaseService.addChatId(message.getChatId(), message.getFrom().getId());
                } else {
                    sendMessage.setText("I have no money. You Chat Id: " + message.getChatId());
                    sendMessage.setReplayMarkup(keyboardMarkup);
                }
            }
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(sendMessage);
        } else {
            sendMessage.setText("Hey. What's up?");
            sendMessage.setReplayMarkup(keyboardMarkup);
            return ResponseEntity.ok(sendMessage);
        }
    }
}
