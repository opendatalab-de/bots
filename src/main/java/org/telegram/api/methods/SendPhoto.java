package org.telegram.api.methods;

import org.telegram.api.objects.ReplyKeyboard;

/**
 * @author Ruben Bermudez
 * @version 1.0
 * @brief Use this method to send photos. On success, the sent Message is returned.
 * @date 20 of June of 2015
 */
public class SendPhoto {

    public static final String PATH = "sendphoto";
    public static final String CHATID_FIELD = "chat_id";
    private Integer chatId; ///< Unique identifier for the message recepient — User or GroupChat id
    public static final String PHOTO_FIELD = "photo";
    private String photo; ///< Photo to send. file_id as String to resend a photo that is already on the Telegram servers
    public static final String CAPTION_FIELD = "photo";
    private String caption; ///< Optional Photo caption (may also be used when resending photos by file_id).
    public static final String REPLYTOMESSAGEID_FIELD = "reply_to_message_id";
    private Integer replayToMessageId; ///< Optional. If the message is a reply, ID of the original message
    public static final String REPLYMARKUP_FIELD = "reply_markup";
    private ReplyKeyboard replayMarkup; ///< Optional. JSON-serialized object for a custom reply keyboard
    private boolean isNewPhoto; ///< True if the photo must be uploaded from a file, file if it is a fileId
    private String photoName; ///< Name of the photo

    public SendPhoto() {
        super();
    }

    public Integer getChatId() {
        return chatId;
    }

    public void setChatId(Integer chatId) {
        this.chatId = chatId;
    }

    public String getPhoto() {
        return photo;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
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

    public boolean isNewPhoto() {
        return isNewPhoto;
    }

    public String getPhotoName() {
        return photoName;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
        this.isNewPhoto = false;
    }

    public void setNewPhoto(String photo, String photoName) {
        this.photo = photo;
        this.isNewPhoto = true;
        this.photoName = photoName;
    }
}
