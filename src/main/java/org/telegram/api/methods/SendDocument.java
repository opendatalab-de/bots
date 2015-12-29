package org.telegram.api.methods;

import org.telegram.api.objects.ReplyKeyboard;

/**
 * @author Ruben Bermudez
 * @version 1.0
 * @brief Use this method to send general files. On success, the sent Message is returned.
 * @date 20 of June of 2015
 */
public class SendDocument {

    public static final String PATH = "senddocument";
    public static final String CHATID_FIELD = "chat_id";
    private Integer chatId; ///< Unique identifier for the message recepient — User or GroupChat id
    public static final String DOCUMENT_FIELD = "document";
    private String document; ///< File file to send. file_id as String to resend a file that is already on the Telegram servers
    public static final String REPLYTOMESSAGEID_FIELD = "reply_to_message_id";
    private Integer replayToMessageId; ///< Optional. If the message is a reply, ID of the original message
    public static final String REPLYMARKUP_FIELD = "reply_markup";
    private ReplyKeyboard replayMarkup; ///< Optional. JSON-serialized object for a custom reply keyboard
    private boolean isNewDocument;
    private String documentName;

    public SendDocument() {
        super();
    }

    public Integer getChatId() {
        return chatId;
    }

    public void setChatId(Integer chatId) {
        this.chatId = chatId;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
        this.isNewDocument = false;
    }

    public void setNewDocument(String document, String documentName) {
        this.document = document;
        this.isNewDocument = true;
        this.documentName = documentName;
    }

    public boolean isNewDocument() {
        return isNewDocument;
    }

    public String getDocumentName() {
        return documentName;
    }

    public Integer getReplayToMessageId() {
        return replayToMessageId;
    }

    public void setReplayToMessageId(Integer replayToMessageId) {
        this.replayToMessageId = replayToMessageId;
    }

    public ReplyKeyboard getReplayMarkup() {
        return replayMarkup;
    }

    public void setReplayMarkup(ReplyKeyboard replayMarkup) {
        this.replayMarkup = replayMarkup;
    }
}
