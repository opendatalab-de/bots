package de.grundid.api.telegram.onedollar;

public class UserAndChatId {

    private Integer chatId;
    private Integer userId;

    public UserAndChatId(Integer chatId, Integer userId) {
        this.chatId = chatId;
        this.userId = userId;
    }

    public UserAndChatId() {
    }

    public Integer getChatId() {
        return chatId;
    }

    public void setChatId(Integer chatId) {
        this.chatId = chatId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
