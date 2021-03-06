package org.telegram.api.objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import org.json.JSONObject;
import org.telegram.api.interfaces.BotApiObject;

import java.io.IOException;

/**
 * @author Ruben Bermudez
 * @version 1.0
 * @brief This object represents a Telegram chat with an user or a group
 * @date 24 of June of 2015
 */
public class Chat implements BotApiObject {

    public static final String ID_FIELD = "id";
    @JsonProperty(ID_FIELD)
    private Integer id; ///< Unique identifier for this chat
    public static final String TITLE_FIELD = "title";
    @JsonProperty(TITLE_FIELD)
    private String title; ///< Group name
    public static final String FIRSTNAME_FIELD = "first_name";
    @JsonProperty(FIRSTNAME_FIELD)
    private String firstName; ///< User‘s or bot’s first name
    public static final String LASTNAME_FIELD = "last_name";
    @JsonProperty(LASTNAME_FIELD)
    private String lastName; ///< Optional. User‘s or bot’s last name
    public static final String USERNAME_FIELD = "username";
    @JsonProperty(USERNAME_FIELD)
    private String userName; ///< Optional. User‘s or bot’s username

    public Chat() {
        super();
    }

    public Chat(JSONObject jsonObject) {
        super();
        this.id = jsonObject.getInt(ID_FIELD);
        if (this.id > 0) {
            this.firstName = jsonObject.getString(FIRSTNAME_FIELD);
            if (jsonObject.has(LASTNAME_FIELD)) {
                this.lastName = jsonObject.getString(LASTNAME_FIELD);
            }
            if (jsonObject.has(USERNAME_FIELD)) {
                this.userName = jsonObject.getString(USERNAME_FIELD);
            }
        }
        else {
            this.title = jsonObject.getString(TITLE_FIELD);
        }
    }

    public Integer getId() {
        return id;
    }

    public Boolean isGroupChat() {
        if (id < 0) {
            return true;
        }
        else {
            return false;
        }
    }

    public String getTitle() {
        return title;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUserName() {
        return userName;
    }

    @Override
    public void serialize(JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeNumberField(ID_FIELD, id);
        if (id > 0) {
            gen.writeStringField(FIRSTNAME_FIELD, firstName);
            gen.writeStringField(LASTNAME_FIELD, lastName);
            gen.writeStringField(USERNAME_FIELD, userName);
        }
        else {
            gen.writeStringField(TITLE_FIELD, title);
        }
        gen.writeEndObject();
        gen.flush();
    }

    @Override
    public void serializeWithType(JsonGenerator gen, SerializerProvider serializers, TypeSerializer typeSer)
            throws IOException {
        gen.writeStartObject();
        gen.writeNumberField(ID_FIELD, id);
        if (id > 0) {
            gen.writeStringField(FIRSTNAME_FIELD, firstName);
            gen.writeStringField(LASTNAME_FIELD, lastName);
            gen.writeStringField(USERNAME_FIELD, userName);
        }
        else {
            gen.writeStringField(TITLE_FIELD, title);
        }
        gen.writeEndObject();
        gen.flush();
    }
}
