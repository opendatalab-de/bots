package de.grundid.api.telegram.drinker;

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

@RestController
public class DrinkerBotController {

    private static Logger log = LoggerFactory.getLogger(DrinkerBotController.class);
    private static final String BOT_OWNER = "nitegate";
    @Autowired
    private DrinkerDatabaseService drinkerDatabaseService;
    @Autowired
    private DrinkerUpdateService drinkerUpdateService;

    @RequestMapping(value = "/bot/drinker", method = RequestMethod.POST)
    public ResponseEntity<?> post(@RequestBody Update update) {
        Message message = update.getMessage();
        String username = message.getFrom() != null ? message.getFrom().getUserName() : null;
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId());
        if (StringUtils.hasText(message.getText())) {
            try {
                CommandParser commandParser = new CommandParser(message.getText());
                String command = commandParser.getCommand();
                if ("start".equals(command)) {
                    drinkerDatabaseService.addChatId(message.getChatId());
                    sendMessage.setText(
                            "Willkommen. Ich werde dir jetzt automatisch Nachrichten mit neuen Drinks aus der Drinker App schicken.");
                }
                else if ("stop".equals(command)) {
                    drinkerDatabaseService.removeChatId(message.getChatId());
                    sendMessage.setText(
                            "Alles klar. Keine Nachrichten mehr.");
                }
                else if ("adminchat".equals(command)) {
                    log.info("admin chat request from {}", username);
                    if (BOT_OWNER.equals(username)) {
                        drinkerDatabaseService.addAdminChatId(message.getChatId());
                        sendMessage.setText(
                                "Ok, dieser Chat wurde zum Admin-Chat hochgestuft. Ich schicke jetzt Zusatzinformationen zu den Drinks.");
                    }
                    else {
                        sendMessage.setText(
                                "Sorry, aber du bist nicht mein Meister.");
                    }
                }
                else if ("last".equals(command)) {
                    int daysBack = 1;
                    if (commandParser.hasParams()) {
                        try {
                            daysBack = Integer.parseInt(commandParser.getParams().get(0));
                        }
                        catch (NumberFormatException e) {
                            log.error(e.getMessage(), e);
                        }
                    }
                    if (BOT_OWNER.equals(username)) {
                        sendMessage.setText(
                                drinkerUpdateService
                                        .getChangedDrinksSince(
                                                System.currentTimeMillis() - daysBack * 24 * 60 * 60 * 1000,
                                                message.getChatId()));
                        sendMessage.enableMarkdown(true);
                    }
                    else {
                        sendMessage.setText(
                                "Sorry, aber du bist nicht mein Meister.");
                    }
                }
                else {
                    sendMessage.setText(
                            "Sorry, aber der Befehl ist mir unbekannt.");
                }
            }
            catch (ParseException e) {
                sendMessage.setText(
                        "Befehl ungültig");
            }
        }
        else {
            sendMessage.setText("Sorry, aber ich habe dich nicht verstanden. Verwende /stop um den Bot zu beenden.");
        }
        return ResponseEntity.ok(sendMessage);
    }
}
