package de.grundid.api.telegram.drinker;

import java.util.concurrent.ConcurrentSkipListSet;

public class Database {

    private ConcurrentSkipListSet<Integer> registeredChatIds = new ConcurrentSkipListSet<>();
    private long lastCheck = System.currentTimeMillis();

    public ConcurrentSkipListSet<Integer> getRegisteredChatIds() {
        return registeredChatIds;
    }

    public void setRegisteredChatIds(ConcurrentSkipListSet<Integer> registeredChatIds) {
        this.registeredChatIds = registeredChatIds;
    }

    public long getLastCheck() {
        return lastCheck;
    }

    public void setLastCheck(long lastCheck) {
        this.lastCheck = lastCheck;
    }
}
