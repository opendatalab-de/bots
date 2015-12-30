package de.grundid.api.telegram.drinker;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
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
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

@Service
public class DrinkerDatabaseService {

    private static Logger log = LoggerFactory.getLogger(DrinkerDatabaseService.class);
    private Set<Integer> registeredChatIds = new ConcurrentSkipListSet<>();
    @Value("${datastoreDir}")
    private String datastoreDir;

    public void addChatId(Integer charId) {
        registeredChatIds.add(charId);
        saveData();
    }

    public void removeChatId(Integer chatId) {
        registeredChatIds.remove(chatId);
        saveData();
    }

    public Collection<Integer> getChatIds() {
        return registeredChatIds;
    }

    @PostConstruct
    public void loadData() {
        File dataFile = new File(datastoreDir + File.separator + "drinker-chatids.json");
        if (dataFile.exists()) {
            try {
                FileInputStream inputStream = new FileInputStream(dataFile);
                Set<Integer> knownIds = new ObjectMapper().readValue(inputStream,
                        CollectionType.construct(HashSet.class, SimpleType.construct(Integer.class)));
                registeredChatIds.addAll(knownIds);
                inputStream.close();
                log.info("Saved chatIds loaded");
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveData() {
        try {
            File dataFile = new File(datastoreDir + File.separator + "drinker-chatids.json");
            FileOutputStream out = new FileOutputStream(dataFile);
            new ObjectMapper().writerWithDefaultPrettyPrinter().writeValue(out, registeredChatIds);
            out.flush();
            out.close();
            log.info("ChatIds saved");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
