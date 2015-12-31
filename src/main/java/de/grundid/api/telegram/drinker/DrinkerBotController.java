package de.grundid.api.telegram.drinker;

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

@RestController
public class DrinkerBotController {

    private static final String BOT_OWNER = "@nitegate";
    @Autowired
    private DrinkerDatabaseService drinkerDatabaseService;

    @RequestMapping(value = "/bot/drinker", method = RequestMethod.POST)
    public ResponseEntity<?> post(@RequestBody Update update) {
        Message message = update.getMessage();
        String username = message.getFrom() != null ? message.getFrom().getUserName() : null;
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId());
        if (StringUtils.hasText(message.getText())) {
            String userMessage = message.getText().toLowerCase();
            if (userMessage.startsWith("/start")) {
                drinkerDatabaseService.addChatId(message.getChatId());
                sendMessage.setText(
                        "Willkommen. Ich werde dir jetzt automatisch Nachrichten mit neuen Drinks aus der Drinker App schicken.");
            }
            else if (userMessage.startsWith("/stop")) {
                drinkerDatabaseService.removeChatId(message.getChatId());
                sendMessage.setText(
                        "Alles klar. Keine Nachrichten mehr.");
            }
            else if (userMessage.startsWith("/adminChat")) {
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
        }
        else {
            sendMessage.setText("Sorry, aber ich habe dich nicht verstanden. Verwende /stop um den Bot zu beenden.");
        }
        return ResponseEntity.ok(sendMessage);
    }
}
