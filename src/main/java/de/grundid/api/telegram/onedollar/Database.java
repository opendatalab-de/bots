package de.grundid.api.telegram.onedollar;

import java.util.concurrent.ConcurrentSkipListSet;

public class Database {
    private ConcurrentSkipListSet<UserAndChatId> registeredChatIds = new ConcurrentSkipListSet<>();

    public ConcurrentSkipListSet<UserAndChatId> getRegisteredChatIds() {
        return registeredChatIds;
    }

    public void setRegisteredChatIds(ConcurrentSkipListSet<UserAndChatId> registeredChatIds) {
        this.registeredChatIds = registeredChatIds;
    }
}
