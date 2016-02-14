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
import java.util.*;

@Controller
public class OneDollarBotController {

    private static final char[] MONEY_BAG = Character.toChars(0x1F4B0);
    private static final char[] SMILE_WITH_GLASSES = Character.toChars(0x1F60E);
    private static final String GIVE_ONE_DOLLAR = "I'll give you one dollar " + MONEY_BAG[0] + MONEY_BAG[1];
    private static final String YES_IM_CHRISTIAN =
            "Yes, I'm Christian." + SMILE_WITH_GLASSES[0] + SMILE_WITH_GLASSES[1];
    private static final String YES_IM_VERY_RICH =
            "Yes, I'm very rich." + MONEY_BAG[0] + MONEY_BAG[1] + " " + MONEY_BAG[0] + MONEY_BAG[1] + " " + MONEY_BAG[0]
                    + MONEY_BAG[1];
    private static final String YES_IM_QUITE_RICH =
            "Yes, I'm quite rich." + MONEY_BAG[0] + MONEY_BAG[1] + " " + MONEY_BAG[0] + MONEY_BAG[1];
    private static final String NO_IM_NOT_RICH =
            "No, I'm not rich." + MONEY_BAG[0] + MONEY_BAG[1] + " " + MONEY_BAG[0] + MONEY_BAG[1];
    @Autowired
    private OneDollarDatabaseService oneDollarDatabaseService;
    @Autowired
    private ImageUpdateService imageUpdateService;
    private ReplyKeyboardMarkup areYouRich;
    private ReplyKeyboardMarkup areYouChristian;
    private Set<Integer> knownChristianChats = new HashSet<>();

    @RequestMapping(value = "/bot/onedollar", method = RequestMethod.POST)
    public ResponseEntity<?> post(@RequestBody Update update) throws IOException {
        Message message = update.getMessage();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId());
        ReplyKeyboardMarkup keyboardMarkup = createKeyboard(Arrays.asList(Arrays.asList(GIVE_ONE_DOLLAR)));
        areYouChristian = createKeyboard(Arrays.asList(Arrays.asList("Yes, I'm Christian", "No.")));
        areYouRich = createKeyboard(Arrays.asList(Arrays.asList(YES_IM_VERY_RICH), Arrays.asList(YES_IM_QUITE_RICH),
                Arrays.asList("No, I'm not rich.")));
        if (StringUtils.hasText(message.getText())) {
            if (CommandParser.isCommand(message.getText())) {
                try {
                    CommandParser commandParser = new CommandParser(message.getText());
                    String command = commandParser.getCommand();
                    if ("give".equals(command)) {
                        sendMessage.setText("[Give one dollar with PayPal](http://bit.ly/OneDollarBot)");
                        sendMessage.enableMarkdown(true);
                    }
                    else {
                        sendMessage.setText("Try /give");
                        sendMessage.setReplayMarkup(keyboardMarkup);
                    }
                }
                catch (ParseException e) {
                    sendMessage.setText("Do you have some money for me?");
                    sendMessage.setReplayMarkup(keyboardMarkup);
                }
            }
            else {
                System.out.println(message.getText());
                if (GIVE_ONE_DOLLAR.equals(message.getText())) {
                    sendMessage.setText("[Give one dollar with PayPal](http://bit.ly/OneDollarBot)");
                    sendMessage.enableMarkdown(true);
                    oneDollarDatabaseService.addChatId(message.getChatId(), message.getFrom().getId());
                }
                else {
                    Calendar now = Calendar.getInstance();
                    if (now.get(Calendar.MONTH) == Calendar.FEBRUARY && now.get(Calendar.DAY_OF_MONTH) == 14) {
                        handleBirthday(sendMessage, message.getText());
                        return ResponseEntity.ok(sendMessage);
                    }
                    else {
                        sendMessage.setText("I have no money. You Chat Id: " + message.getChatId());
                        sendMessage.setReplayMarkup(keyboardMarkup);
                        imageUpdateService.sendPhoto("shia01.png", "Good luck", message.getChatId());
                        return ResponseEntity.ok(sendMessage);
                    }
                }
            }
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(sendMessage);
        }
        else {
            sendMessage.setText("Hey. What's up?");
            sendMessage.setReplayMarkup(keyboardMarkup);
            return ResponseEntity.ok(sendMessage);
        }
    }

    private void handleBirthday(SendMessage sendMessage, String message) {
        if (YES_IM_CHRISTIAN.equals(message)) {
            if (knownChristianChats.contains(sendMessage.getChatId())) {
                sendMessage.setText("We have already talked, sir. Have a nice Birthday!");
            }
            else {
                sendMessage.setText("Happy Birthday to you, sir. Are you rich?");
                sendMessage.setReplayMarkup(areYouRich);
            }
        }
        else if (YES_IM_VERY_RICH.equals(message)) {
            sendMessage.setText("[Give five dollar with PayPal](http://bit.ly/OneDollarBot)");
            sendMessage.enableMarkdown(true);
        }
        else if (YES_IM_QUITE_RICH.equals(message)) {
        }
        else if (NO_IM_NOT_RICH.equals(message)) {
            sendMessage.setText("You should start making good Apps, sir. Talk to Adrian.");
            knownChristianChats.add(sendMessage.getChatId());
        }
        else {
            sendMessage.setText("Good day to you, sir. Are you Christian?");
            sendMessage.setReplayMarkup(areYouChristian);
        }
    }

    private ReplyKeyboardMarkup createKeyboard(List<List<String>> keyboard) {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setOneTimeKeyboad(true);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setKeyboard(keyboard);
        return keyboardMarkup;
    }
}
