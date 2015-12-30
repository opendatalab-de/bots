package de.grundid.api.telegram.drinker;

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

@Service
public class DrinkerDatabaseService {

    private static Logger log = LoggerFactory.getLogger(DrinkerDatabaseService.class);
    private Database database = new Database();
    @Value("${datastoreDir}")
    private String datastoreDir;

    public void addChatId(Integer charId) {
        database.getRegisteredChatIds().add(charId);
        saveData();
    }

    public void removeChatId(Integer chatId) {
        database.getRegisteredChatIds().remove(chatId);
        saveData();
    }

    public long getLastCheck() {
        return database.getLastCheck();
    }

    public void setLastCheck(long lastCheck) {
        database.setLastCheck(lastCheck);
        saveData();
    }

    public Collection<Integer> getChatIds() {
        return database.getRegisteredChatIds();
    }

    @PostConstruct
    public void loadData() {
        File dataFile = new File(datastoreDir + File.separator + "drinker-database.json");
        if (dataFile.exists()) {
            try {
                FileInputStream inputStream = new FileInputStream(dataFile);
                database = new ObjectMapper().readValue(inputStream,
                        SimpleType.construct(Database.class));
                inputStream.close();
                log.info("Saved database loaded");
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveData() {
        try {
            File dataFile = new File(datastoreDir + File.separator + "drinker-database.json");
            FileOutputStream out = new FileOutputStream(dataFile);
            new ObjectMapper().writerWithDefaultPrettyPrinter().writeValue(out, database);
            out.flush();
            out.close();
            log.info("Database saved");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
