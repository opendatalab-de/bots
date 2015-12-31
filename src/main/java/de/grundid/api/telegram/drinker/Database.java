package de.grundid.api.telegram.drinker;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.concurrent.ConcurrentSkipListSet;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Database {

    private ConcurrentSkipListSet<Integer> registeredChatIds = new ConcurrentSkipListSet<>();
    private ConcurrentSkipListSet<Integer> adminChatIds = new ConcurrentSkipListSet<>();
    private long lastCheck = System.currentTimeMillis();

    public ConcurrentSkipListSet<Integer> getRegisteredChatIds() {
        return registeredChatIds;
    }

    public void setRegisteredChatIds(ConcurrentSkipListSet<Integer> registeredChatIds) {
        this.registeredChatIds = registeredChatIds;
    }

    public ConcurrentSkipListSet<Integer> getAdminChatIds() {
        return adminChatIds;
    }

    public void setAdminChatIds(ConcurrentSkipListSet<Integer> adminChatIds) {
        this.adminChatIds = adminChatIds;
    }

    public long getLastCheck() {
        return lastCheck;
    }

    public void setLastCheck(long lastCheck) {
        this.lastCheck = lastCheck;
    }
}
