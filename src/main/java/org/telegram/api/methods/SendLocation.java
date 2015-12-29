package org.telegram.api.methods;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import org.json.JSONObject;
import org.telegram.api.objects.Message;
import org.telegram.api.objects.ReplyKeyboard;

import java.io.IOException;

/**
 * @author Ruben Bermudez
 * @version 1.0
 * @brief Use this method to send point on the map. On success, the sent Message is returned.
 * @date 20 of June of 2015
 */
public class SendLocation extends BotApiMethod<Message> {

    public static final String PATH = "sendlocation";
    public static final String CHATID_FIELD = "chat_id";
    private Integer chatId; ///< Unique identifier for the message recepient — User or GroupChat id
    public static final String LATITUDE_FIELD = "latitude";
    private Float latitude; ///< Latitude of location
    public static final String LONGITUDE_FIELD = "longitude";
    private Float longitude; ///< Longitude of location
    public static final String REPLYTOMESSAGEID_FIELD = "reply_to_message_id";
    private Integer replayToMessageId; ///< Optional. If the message is a reply, ID of the original message
    public static final String REPLYMARKUP_FIELD = "reply_markup";
    private ReplyKeyboard replayMarkup; ///< Optional. JSON-serialized object for a custom reply keyboard

    public Integer getChatId() {
        return chatId;
    }

    public void setChatId(Integer chatId) {
        this.chatId = chatId;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
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

    @Override
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(CHATID_FIELD, chatId);
        jsonObject.put(LATITUDE_FIELD, latitude);
        jsonObject.put(LONGITUDE_FIELD, longitude);
        if (replayToMessageId != null) {
            jsonObject.put(REPLYTOMESSAGEID_FIELD, replayToMessageId);
        }
        if (replayMarkup != null) {
            jsonObject.put(REPLYMARKUP_FIELD, replayMarkup.toJson());
        }
        return jsonObject;
    }

    @Override
    public void serialize(JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeStringField(METHOD_FIELD, PATH);
        gen.writeNumberField(CHATID_FIELD, chatId);
        gen.writeNumberField(LATITUDE_FIELD, latitude);
        gen.writeNumberField(LONGITUDE_FIELD, longitude);
        if (replayToMessageId != null) {
            gen.writeNumberField(REPLYTOMESSAGEID_FIELD, replayToMessageId);
        }
        if (replayMarkup != null) {
            gen.writeObjectField(REPLYMARKUP_FIELD, replayMarkup);
        }
        gen.writeEndObject();
        gen.flush();
    }

    @Override
    public void serializeWithType(JsonGenerator gen, SerializerProvider serializers, TypeSerializer typeSer)
            throws IOException {
        gen.writeStartObject();
        gen.writeStringField(METHOD_FIELD, PATH);
        gen.writeNumberField(CHATID_FIELD, chatId);
        gen.writeNumberField(LATITUDE_FIELD, latitude);
        gen.writeNumberField(LONGITUDE_FIELD, longitude);
        if (replayToMessageId != null) {
            gen.writeNumberField(REPLYTOMESSAGEID_FIELD, replayToMessageId);
        }
        if (replayMarkup != null) {
            gen.writeObjectField(REPLYMARKUP_FIELD, replayMarkup);
        }
        gen.writeEndObject();
        gen.flush();
    }
}
