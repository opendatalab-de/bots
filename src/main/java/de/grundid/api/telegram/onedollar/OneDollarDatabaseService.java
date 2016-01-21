package de.grundid.api.telegram.onedollar;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.SimpleType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

@Service
public class OneDollarDatabaseService {

    private static Logger log = LoggerFactory.getLogger(OneDollarDatabaseService.class);
    private Database database = new Database();
    @Value("${datastoreDir}")
    private String datastoreDir;

    public void addChatId(Integer chatId, Integer userId) {
        database.getRegisteredChatIds().add(new UserAndChatId(chatId, userId));
        saveData();
    }

    public Collection<UserAndChatId> chatIds() {
        return database.getRegisteredChatIds();
    }


    public void removeChatId(Integer chatId) {
        for (Iterator<UserAndChatId> iterator = database.getRegisteredChatIds().iterator(); iterator.hasNext(); ) {
            UserAndChatId userAndChatId = iterator.next();
            if (userAndChatId.getChatId().equals(chatId)) {
                iterator.remove();
            }
        }
        saveData();
    }

    @PostConstruct
    public void loadData() {
        File dataFile = new File(datastoreDir + File.separator + "onedollar-database.json");
        if (dataFile.exists()) {
            try {
                FileInputStream inputStream = new FileInputStream(dataFile);
                database = new ObjectMapper().readValue(inputStream,
                        SimpleType.construct(Database.class));
                inputStream.close();
                log.info("Saved database loaded");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveData() {
        try {
            File dataFile = new File(datastoreDir + File.separator + "onedollar-database.json");
            FileOutputStream out = new FileOutputStream(dataFile);
            new ObjectMapper().writerWithDefaultPrettyPrinter().writeValue(out, database);
            out.flush();
            out.close();
            log.info("Database saved");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
