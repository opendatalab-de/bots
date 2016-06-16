package de.grundid.api.telegram.farmbothn;

import de.grundid.api.telegram.CommandParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.telegram.api.methods.SendMessage;
import org.telegram.api.objects.Message;
import org.telegram.api.objects.Update;

import java.text.ParseException;


@RestController
public class FarmbotController {

    @Autowired
    private FarmbotDatabaseService databaseService;

    @RequestMapping(value = "/bot/farmbotHn", method = RequestMethod.POST)
    public ResponseEntity<?> post(@RequestBody Update update){
        Message message = update.getMessage();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId());
        if(StringUtils.hasText(message.getText())){
            try {
                CommandParser parser = new CommandParser(message.getText());
                String command = parser.getCommand();
                if("start".equals(command)) {
                    databaseService.addChatId(message.getChatId());
                    sendMessage.setText("Ich habe mir diesen Chat gemerkt. Du bekommst jetzt Status-Updates vom Farmbot");
                } else if("stop".equals(command)){
                    databaseService.removeChatId(message.getChatId());
                    sendMessage.setText("Ok. Keine weiteren Nachrichten");
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if(sendMessage.getText() != null){
            return ResponseEntity.ok().body(sendMessage);
        } else {
            return  ResponseEntity.noContent().build();
        }
    }
}
