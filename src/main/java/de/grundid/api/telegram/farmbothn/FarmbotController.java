package de.grundid.api.telegram.farmbothn;

import de.grundid.api.telegram.CommandParser;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.telegram.api.methods.Constants;
import org.telegram.api.methods.SendMessage;
import org.telegram.api.objects.Message;
import org.telegram.api.objects.Update;

import java.text.ParseException;
import java.util.HashSet;
import java.util.Set;


@RestController
public class FarmbotController {

    @Value("${telegram.farmbotHn.apiKey}")
    private String apiKey;

    private RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private FarmbotDatabaseService databaseService;
    @Autowired
    private FarmbotUpdateService updateService;

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


    //Sends Message when he gets a message from the pi
    @RequestMapping(value = "/bot/farmbotHn/post", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    public void dataPosted(@RequestBody FarmbotValue value){

        updateService.setLastPercent(Double.parseDouble(value.getPercent()));

        Set<Integer> chatIdsToRemove = new HashSet<>();
        for (Integer chatID : databaseService.getChatIds()) {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatID);
            sendMessage.setText(value.getPercent());

            try {
                restTemplate.postForEntity(Constants.BASEURL + apiKey + "/" + SendMessage.PATH, sendMessage, String.class);
            } catch (Exception e) {
                chatIdsToRemove.add(chatID);
            }

        }

        for (Integer chatID : chatIdsToRemove) {
            databaseService.removeChatId(chatID);
        }
    }
}
