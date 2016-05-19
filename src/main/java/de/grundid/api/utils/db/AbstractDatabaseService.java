package de.grundid.api.utils.db;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.SimpleType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;

public abstract class AbstractDatabaseService {

    private static Logger log = LoggerFactory.getLogger(AbstractDatabaseService.class);
    private Database database = new Database();
    @Value("${datastoreDir}")
    private String datastoreDir;
    private String fileName;

    public AbstractDatabaseService(String fileName) {
        this.fileName = fileName;
    }

    public void addChatId(Integer chatId) {
        database.getRegisteredChatIds().add(chatId);
        saveData();
    }

    public void removeChatId(Integer chatId) {
        database.getRegisteredChatIds().remove(chatId);
        database.getAdminChatIds().remove(chatId);
        saveData();
    }

    public void addAdminChatId(Integer chatId) {
        database.getAdminChatIds().add(chatId);
        saveData();
    }

    public long getLastCheck() {
        return database.getLastCheck();
    }

    public void setLastCheck(long lastCheck) {
        database.setLastCheck(lastCheck);
        saveData();
    }

    public boolean isAdminChat(Integer chatId) {
        return database.getAdminChatIds().contains(chatId);
    }

    public Collection<Integer> getChatIds() {
        return database.getRegisteredChatIds();
    }

    @PostConstruct
    public void loadData() {
        File dataFile = new File(datastoreDir + File.separator + fileName);
        if (dataFile.exists()) {
            try {
                FileInputStream inputStream = new FileInputStream(dataFile);
                database = new ObjectMapper().readValue(inputStream, SimpleType.construct(Database.class));
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
            File dataFile = new File(datastoreDir + File.separator + fileName);
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
