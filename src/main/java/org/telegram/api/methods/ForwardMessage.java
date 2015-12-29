package org.telegram.api.methods;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import org.json.JSONObject;
import org.telegram.api.objects.Message;

import java.io.IOException;

/**
 * @author Ruben Bermudez
 * @version 1.0
 * @brief Use this method to send text messages. On success, the sent Message is returned.
 * @date 20 of June of 2015
 */
public class ForwardMessage extends BotApiMethod<Message> {

    public static final String PATH = "forwardmessage";
    public static final String CHATID_FIELD = "chat_id";
    private Integer chatId; ///< Unique identifier for the message recepient — User or GroupChat id
    public static final String FROMCHATID_FIELD = "from_chat_id";
    private Integer fromChatId; ///< Unique identifier for the chat where the original message was sent — User or GroupChat id
    public static final String MESSAGEID_FIELD = "message_id";
    private Integer messageId; ///< Unique message identifier

    public ForwardMessage() {
        super();
    }

    public Integer getChatId() {
        return chatId;
    }

    public void setChatId(Integer chatId) {
        this.chatId = chatId;
    }

    public Integer getFromChatId() {
        return fromChatId;
    }

    public void setFromChatId(Integer fromChatId) {
        this.fromChatId = fromChatId;
    }

    public Integer getMessageId() {
        return messageId;
    }

    public void setMessageId(Integer messageId) {
        this.messageId = messageId;
    }

    @Override
    public void serialize(JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeStringField(METHOD_FIELD, PATH);
        gen.writeNumberField(CHATID_FIELD, chatId);
        gen.writeNumberField(FROMCHATID_FIELD, fromChatId);
        gen.writeNumberField(MESSAGEID_FIELD, messageId);
        gen.writeEndObject();
        gen.flush();
    }

    @Override
    public void serializeWithType(JsonGenerator gen, SerializerProvider serializers, TypeSerializer typeSer)
            throws IOException {
        gen.writeStartObject();
        gen.writeStringField(METHOD_FIELD, PATH);
        gen.writeNumberField(CHATID_FIELD, chatId);
        gen.writeNumberField(FROMCHATID_FIELD, fromChatId);
        gen.writeNumberField(MESSAGEID_FIELD, messageId);
        gen.writeEndObject();
        gen.flush();
    }

    @Override
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(CHATID_FIELD, chatId);
        jsonObject.put(FROMCHATID_FIELD, fromChatId);
        jsonObject.put(MESSAGEID_FIELD, messageId);
        return jsonObject;
    }

    @Override
    public String getPath() {
        return PATH;
    }

    @Override
    public Message deserializeResponse(JSONObject answer) {
        if (answer.getBoolean("ok")) {
            return new Message(answer.getJSONObject("result"));
        }
        return null;
    }
}
